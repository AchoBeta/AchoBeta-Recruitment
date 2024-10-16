package com.achobeta.domain.student.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.service.ResumeStatusProcessService;
import com.achobeta.domain.student.constants.StuResumeConstants;
import com.achobeta.domain.student.model.converter.StuResumeConverter;
import com.achobeta.domain.student.model.dao.mapper.StuResumeMapper;
import com.achobeta.domain.student.model.dto.*;
import com.achobeta.domain.student.model.entity.StuAttachment;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuAttachmentVO;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.model.vo.StuSimpleResumeVO;
import com.achobeta.domain.student.service.StuAttachmentService;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 马拉圈
 * @description 针对表【stu_resume(学生简历表)】的数据库操作Service实现
 * @createDate 2024-07-06 14:20:47
 */
@Service
@RequiredArgsConstructor
public class StuResumeServiceImpl extends ServiceImpl<StuResumeMapper, StuResume>
        implements StuResumeService {

    private final StuAttachmentService stuAttachmentService;
    private final StuResumeConverter stuResumeConverter;
    private final ResumeStatusProcessService resumeStatusProcessService;
    private final ResourceService resourceService;
    private final RedisLock redisLock;
    private final SimpleLockStrategy simpleLockStrategy;

    @Override
    public Optional<StuResume> getStuResume(Long batchId, Long stuId) {
        return this.lambdaQuery()
                .eq(StuResume::getUserId, stuId)
                .eq(StuResume::getBatchId, batchId)
                .oneOpt();
    }

    @Override
    public Optional<StuResume> getStuResume(Long resumeId) {
        return this.lambdaQuery()
                .eq(StuResume::getId, resumeId)
                .oneOpt();
    }

    @Override
    public StuResume checkAndGetStuResumeByBatchIdAndStuId(Long batchId, Long stuId) {
        return getStuResume(batchId, stuId)
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS)
                );
    }

    @Override
    @Transactional
    public void submitResume(StuResumeDTO stuResumeDTO, Long userId) {

        // 使用锁确保唯一性
        redisLock.tryLockDoSomething(StuResumeConstants.RESUME_SUBMIT_LOCK + userId, () -> {
            //检查简历提交否超过最大次数
            StuResume stuResume = checkResumeSubmitCount(stuResumeDTO.getStuSimpleResumeDTO(), userId);

            //简历表信息
            StuSimpleResumeDTO resumeDTO = stuResumeDTO.getStuSimpleResumeDTO();

            // 检测
            Long oldImage = stuResume.getImage();
            Boolean shouldRemove = resourceService.shouldRemove(resumeDTO.getImage(), oldImage);

            //附件列表
            List<StuAttachmentDTO> stuAttachmentDTOList = stuResumeDTO.getStuAttachmentDTOList();

            //是否存在已有简历信息
            Optional.ofNullable(stuResume.getId())
                    .ifPresentOrElse(id -> updateResumeInfo(stuResume, resumeDTO), () -> saveResumeInfo(stuResume, resumeDTO));

            //保存附件信息
            saveStuAttachment(stuAttachmentDTOList, stuResume.getId());

            // 等提交成功后再删除
            if(Boolean.TRUE.equals(shouldRemove)) {
                resourceService.removeKindly(oldImage);
            }
        }, () -> {}, simpleLockStrategy);
    }

    @Override
    public StuResumeVO getResumeInfo(QueryResumeDTO queryResumeDTO) {
        //查询简历信息
        StuResume stuResume = getStuResume(queryResumeDTO);
        //封装返回结果
        return buildStuResumeVO(queryResumeDTO, stuResume);

    }

    @Override
    public StuResume checkAndGetResume(Long resumeId) {
        return getStuResume(resumeId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));
    }

    private StuResumeVO buildStuResumeVO(QueryResumeDTO queryResumeDTO, StuResume stuResume) {
        //返回信息实体
        StuResumeVO stuResumeVO = new StuResumeVO();
        //构造返回的简历表信息
        StuSimpleResumeVO simpleResumeVO = stuResumeConverter.stuResumeToSimpleVO(stuResume);
        //查询并构造附件集合
        List<StuAttachment> stuAttachmentList = stuAttachmentService.listByResumeId(queryResumeDTO.getResumeId());
        List<StuAttachmentVO> stuAttachmentVOList = stuResumeConverter.stuAttachmentsToVOList(stuAttachmentList);

        //将简历表信息和附件信息封装返回
        stuResumeVO.setStuSimpleResumeVO(simpleResumeVO);
        stuResumeVO.setStuAttachmentVOList(stuAttachmentVOList);
        return stuResumeVO;
    }

    private StuResume getStuResume(QueryResumeDTO queryResumeDTO) {
        QueryResumeOfUserDTO resumeOfUserDTO = queryResumeDTO.getQueryResumeOfUserDTO();

        StuResume stuResume;
        //查询简历信息
        if (Objects.nonNull(resumeOfUserDTO)) {
            //根据batchId和userId查询
            stuResume = lambdaQuery()
                    .eq(StuResume::getUserId, resumeOfUserDTO.getUserId())
                    .eq(StuResume::getBatchId, resumeOfUserDTO.getBatchId())
                    .oneOpt().orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));
            queryResumeDTO.setResumeId(stuResume.getId()); // 确保简历附件列表能够被查到
        } else {
            //参数校验
            Optional.ofNullable(queryResumeDTO.getResumeId()).orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.PARAM_IS_BLANK));
            //根据resumeId查询
            stuResume = lambdaQuery()
                    .eq(StuResume::getId, queryResumeDTO.getResumeId())
                    .oneOpt().orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));

        }
        return stuResume;
    }


    @Transactional
    public void updateResumeInfo(StuResume stuResume, StuSimpleResumeDTO resumeDTO) {
        ResumeStatus resumeStatus = stuResume.getStatus();
        if (Objects.isNull(resumeStatus) || ResumeStatus.DRAFT.equals(resumeStatus)) {
            //简历状态若为草稿则更新为待筛选
            stuResume.setStatus(ResumeStatus.PENDING_SELECTION);
            // 添加一个简历过程节点
            resumeStatusProcessService.createResumeStatusProcess(stuResume.getId(), ResumeStatus.PENDING_SELECTION, null);
        }

        stuResumeConverter.updatePoWithStuSimpleResumeDTO(resumeDTO, stuResume);
        //简历提交次数加1
        stuResume.setSubmitCount(stuResume.getSubmitCount() + 1);
        //更新简历信息
        updateById(stuResume);
    }

    @Transactional
    public void saveResumeInfo(StuResume stuResume, StuSimpleResumeDTO resumeDTO) {
        // 设置初始简历状态
        stuResume.setStatus(ResumeStatus.PENDING_SELECTION);
        stuResume.setSubmitCount(1); // 本次提交算一次
        //构建简历实体信息
        stuResumeConverter.updatePoWithStuSimpleResumeDTO(resumeDTO, stuResume);
        //保存简历信息
        save(stuResume);

        // 创建初始的简历状态过程节点
        resumeStatusProcessService.createResumeStatusProcess(stuResume.getId(), ResumeStatus.PENDING_SELECTION, null);
    }

    @Transactional
    public void saveStuAttachment(List<StuAttachmentDTO> stuAttachmentDTOList, Long resumeId) {
        // 待枪毙名单
        Set<Long> oldAttachmentHash = stuAttachmentService.listByResumeId(resumeId).stream()
                .map(StuAttachment::getAttachment)
                .collect(Collectors.toSet());
        //删除原有简历附件
        stuAttachmentService.lambdaUpdate().eq(StuAttachment::getResumeId, resumeId).remove();

        //构造附件保存信息列表
        List<StuAttachment> stuAttachmentList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stuAttachmentDTOList)) {
            //类型转换
            stuAttachmentList = stuAttachmentDTOList.stream().distinct().map(attach -> {
                // 判断是否可以访问这个资源
                resourceService.analyzeCode(attach.getAttachment(), null);
                // 移出枪毙名单
                oldAttachmentHash.remove(attach.getAttachment());
                // 转化
                StuAttachment stuAttachment = stuResumeConverter.stuAttachmentDTOToPo(attach);
                stuAttachment.setResumeId(resumeId);
                return stuAttachment;
            }).toList();

            //批量插入附件信息
            stuAttachmentService.saveBatch(stuAttachmentList);
        }

        if (!CollectionUtils.isEmpty(oldAttachmentHash)) {
            // 这些都是最新的附件里面没有的，挨个枪毙（删除必然只能删除自己的）
            oldAttachmentHash.forEach(resourceService::removeKindly);
        }

    }

    @Override
    public StuResume checkResumeSubmitCount(StuSimpleResumeDTO resumeDTO, Long userId) {

        StuResume stuResume = getStuResume(resumeDTO.getBatchId(), userId).orElseGet(StuResume::new);
        Integer maxSubmitCount = StuResumeConstants.MAX_SUBMIT_COUNT;
        if (stuResume.getId() != null && maxSubmitCount.compareTo(stuResume.getSubmitCount()) <= 0) {
            String message = "提交失败，简历最大提交次数为" + maxSubmitCount;
            throw new GlobalServiceException(message, GlobalServiceStatusCode.USER_RESUME_SUBMIT_OVER_COUNT);
        }
        stuResume.setUserId(userId);
        return stuResume;
    }

    @Override
    public List<StuResume> queryStuList(Long batchId, List<Long> userIds) {
        if(CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return lambdaQuery()
                .eq(StuResume::getBatchId, batchId)
                .in(StuResume::getUserId, userIds)
                .list();
    }
}
