package com.achobeta.domain.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.ResumeStatusEnum;
import com.achobeta.domain.student.model.dao.mapper.StuResumeMapper;
import com.achobeta.domain.student.model.dto.StuAttachmentDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.dto.StuSimpleResumeDTO;
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
    @Transactional
    public void submitResume(StuResumeDTO stuResumeDTO) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        //简历表信息
        StuSimpleResumeDTO resumeDTO = stuResumeDTO.getStuSimpleResumeDTO();

        //附件列表
        List<StuAttachmentDTO> stuAttachmentDTOList = stuResumeDTO.getStuAttachmentDTOList();

        //查询数据库
        StuResume stuResume = getStuResume(resumeDTO.getBatchId(), Long.valueOf(resumeDTO.getStudentId())).orElse(new StuResume());
        //简历状态更新为待筛选
        stuResume.setStatus(ResumeStatusEnum.TO_BE_SCREENED.getResumeStatusCode());
        //是否存在已有简历信息
        if(!Objects.isNull(stuResume)){
            //检查简历提交否超过最大次数
            checkResumeSubmitCount(stuResume.getSubmitCount());
            //更新简历信息
            updateResumeInfo(stuResume,resumeDTO);
        }
        else{
            //保存简历信息
            saveResumeInfo(stuResume, resumeDTO,userId);
        }

        //保存附件信息
        saveStuAttachment(stuAttachmentDTOList, stuResume.getId());
    }

    @Override
    public StuResumeVO getResumeInfo(Long resumeId) {

        StuResumeVO stuResumeVO = buildStuResumeVO(resumeId);

        return stuResumeVO;
    }

    private StuResumeVO buildStuResumeVO(Long resumeId) {
        //返回信息实体
        StuResumeVO stuResumeVO = new StuResumeVO();
        //查询简历信息
        StuResume stuResume = lambdaQuery().eq(StuResume::getId, resumeId).one();
        //判断简历是否存在
        Optional.ofNullable(stuResume).orElseThrow(()->new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));
        //构造返回的简历表信息
        StuSimpleResumeVO simpleResumeVO = new StuSimpleResumeVO();
        BeanUtil.copyProperties(stuResume,simpleResumeVO);

        //查询并构造附件集合
        List<StuAttachment> stuAttachmentList = stuAttachmentService.lambdaQuery().eq(StuAttachment::getResumeId, resumeId).list();
        List<StuAttachmentVO> stuAttachmentVOList = BeanUtil.copyToList(stuAttachmentList, StuAttachmentVO.class);

        //封装简历返回结果
        stuResumeVO.setStuSimpleResumeVO(simpleResumeVO);
        stuResumeVO.setStuAttachmentVOList(stuAttachmentVOList);

        return stuResumeVO;
    }

    private void updateResumeInfo(StuResume stuResume,StuSimpleResumeDTO resumeDTO) {
        BeanUtil.copyProperties(resumeDTO,stuResume);
        //简历提交次数加1
        stuResume.setSubmitCount(stuResume.getSubmitCount()+1);
        //更新简历信息
        updateById(stuResume);
    }

    private void saveResumeInfo(StuResume stuResume,StuSimpleResumeDTO resumeDTO,Long userId) {
        //构建简历实体信息
        BeanUtil.copyProperties(resumeDTO, stuResume);
        stuResume.setUserId(userId);
        //保存简历信息
        save(stuResume);

    }

    private void saveStuAttachment(List<StuAttachmentDTO> stuAttachmentDTOList, Long resumeId) {
        //删除原有简历附件
        stuAttachmentService.lambdaUpdate().eq(StuAttachment::getResumeId, resumeId).remove();
        //构造附件保存信息列表
        List<StuAttachment> stuAttachmentList = new ArrayList<>();
        if (!stuAttachmentList.isEmpty()) {
            stuAttachmentList = stuAttachmentDTOList.stream().map(attach -> {
                StuAttachment stuAttachment = new StuAttachment();
                BeanUtil.copyProperties(attach, stuAttachment);
                stuAttachment.setResumeId(resumeId);
                return stuAttachment;
            }).toList();
        }
        //批量插入附件信息
        stuAttachmentService.saveBatch(stuAttachmentList);
    }

    private void checkResumeSubmitCount(Integer submitCount) {
        if (submitCount >= MAX_SUBMIT_COUNT) {
            String message = "提交失败，简历最大提交次数为" + MAX_SUBMIT_COUNT;
            throw new GlobalServiceException(message, GlobalServiceStatusCode.USER_RESUME_SUBMIT_OVER_COUNT);
        }
    }
}




