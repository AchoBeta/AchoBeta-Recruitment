package com.achobeta.domain.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.ResumeStatusEnum;
import com.achobeta.domain.student.model.converter.StuResumeConverter;
import com.achobeta.domain.student.model.dao.mapper.StuResumeMapper;
import com.achobeta.domain.student.model.dto.*;
import com.achobeta.domain.student.model.entity.StuAttachment;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.achobeta.domain.student.model.vo.StuAttachmentVO;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.model.vo.StuSimpleResumeVO;
import com.achobeta.domain.student.service.StuAttachmentService;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    //简历最大提交数
    private final Integer MAX_SUBMIT_COUNT = 3;

    @Override
    public Optional<StuResume> getStuResume(Long batchId, Long stuId) {
        return this.lambdaQuery()
                .eq(StuResume::getUserId, stuId)
                .eq(StuResume::getBatchId, batchId)
                .oneOpt();
    }

    @Override
    public SimpleStudentVO getSimpleStuResume(Long batchId, Long stuId) {
        return getStuResume(batchId, stuId)
                .map(stuResume -> BeanUtil.copyProperties(stuResume, SimpleStudentVO.class))
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));
    }

    @Override
    public Integer getGradeByBatchIdAndStuId(Long batchId, Long stuId) {
        return getStuResume(batchId, stuId)
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS))
                .getGrade();
    }

    @Override
    public void submitResume(StuResumeDTO stuResumeDTO, StuResume stuResume) {

        Long userId = BaseContext.getCurrentUser().getUserId();
        //简历表信息
        StuSimpleResumeDTO resumeDTO = stuResumeDTO.getStuSimpleResumeDTO();
        //附件列表
        List<StuAttachmentDTO> stuAttachmentDTOList = stuResumeDTO.getStuAttachmentDTOList();

        //简历状态更新为待筛选
        stuResume.setStatus(ResumeStatusEnum.TO_BE_SCREENED.getResumeStatusCode());
        //是否存在已有简历信息
        Optional.ofNullable(stuResume.getId()).
                ifPresentOrElse(id->updateResumeInfo(stuResume, resumeDTO),()->saveResumeInfo(stuResume, resumeDTO, userId));

        //保存附件信息
        saveStuAttachment(stuAttachmentDTOList, stuResume.getId());
    }

    @Override
    public StuResumeVO getResumeInfo(QueryResumeDTO queryResumeDTO) {
        //查询简历信息
        StuResume stuResume = getStuResume(queryResumeDTO);
        //封装返回结果
        return buildStuResumeVO(queryResumeDTO, stuResume);

    }

    private StuResumeVO buildStuResumeVO(QueryResumeDTO queryResumeDTO, StuResume stuResume) {
        //返回信息实体
        StuResumeVO stuResumeVO = new StuResumeVO();
        //构造返回的简历表信息
        StuSimpleResumeVO simpleResumeVO=stuResumeConverter.stuResumeToSimpleVO(stuResume);
        //查询并构造附件集合
        List<StuAttachment> stuAttachmentList = stuAttachmentService.lambdaQuery().eq(StuAttachment::getResumeId, queryResumeDTO.getResumeId()).list();
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
                    .eq( StuResume::getId, queryResumeDTO.getResumeId())
                    .eq( StuResume::getUserId, resumeOfUserDTO.getUserId())
                    .eq( StuResume::getBatchId, resumeOfUserDTO.getBatchId())
                    .oneOpt().orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));

        } else {
            //参数校验
            Optional.ofNullable(queryResumeDTO.getResumeId()).orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.PARAM_IS_BLANK));
            //根据resumeId查询
            stuResume=lambdaQuery()
                    .eq( StuResume::getId, queryResumeDTO.getResumeId())
                    .oneOpt().orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));

        }
        return stuResume;
    }


    private void updateResumeInfo(StuResume stuResume, StuSimpleResumeDTO resumeDTO) {

        stuResumeConverter.updatePoWithStuSimpleResumeDTO(resumeDTO,stuResume);
        //简历提交次数加1
        stuResume.setSubmitCount(stuResume.getSubmitCount() + 1);
        //更新简历信息
        updateById(stuResume);
    }

    private void saveResumeInfo(StuResume stuResume, StuSimpleResumeDTO resumeDTO, Long userId) {
        //构建简历实体信息
        stuResumeConverter.updatePoWithStuSimpleResumeDTO(resumeDTO,stuResume);
        stuResume.setUserId(userId);
        //保存简历信息
        save(stuResume);

    }

    @Transactional
    public void saveStuAttachment(List<StuAttachmentDTO> stuAttachmentDTOList, Long resumeId) {
        //删除原有简历附件
        stuAttachmentService.lambdaUpdate().eq(StuAttachment::getResumeId, resumeId).remove();
        //构造附件保存信息列表
        List<StuAttachment> stuAttachmentList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(stuAttachmentList)) {
            //类型转换
            stuAttachmentList = stuAttachmentDTOList.stream().map(attach -> {
                StuAttachment stuAttachment = stuResumeConverter.stuAttachmentDTOToPo(attach);
                stuAttachment.setResumeId(resumeId);
                return stuAttachment;
            }).toList();

            //批量插入附件信息
            stuAttachmentService.saveBatch(stuAttachmentList);
        }

    }

    @Override
    public StuResume checkResumeSubmitCount(StuSimpleResumeDTO resumeDTO, Long userId) {

        StuResume stuResume = getStuResume(resumeDTO.getBatchId(), Long.valueOf(userId)).orElse(new StuResume());

        if (stuResume.getId() != null && stuResume.getSubmitCount() > MAX_SUBMIT_COUNT) {
            String message = "提交失败，简历最大提交次数为" + MAX_SUBMIT_COUNT;
            throw new GlobalServiceException(message, GlobalServiceStatusCode.USER_RESUME_SUBMIT_OVER_COUNT);
        }
        return stuResume;
    }
}




