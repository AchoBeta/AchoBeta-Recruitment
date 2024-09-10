package com.achobeta.domain.student.service;

import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.dto.StuSimpleResumeDTO;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【stu_resume(学生简历表)】的数据库操作Service
* @createDate 2024-07-06 14:20:47
*/
public interface StuResumeService extends IService<StuResume> {

    Optional<StuResume> getStuResume(Long batchId, Long stuId);

    Optional<StuResume> getStuResume(Long resumeId);

    StuResume checkAndGetStuResumeByBatchIdAndStuId(Long batchId, Long stuId);

    @Transactional
    void submitResume(StuResumeDTO stuResumeDTO,StuResume stuResume);

    StuResumeVO getResumeInfo(QueryResumeDTO queryResumeDTO);

    StuResume checkAndGetResume(Long resumeId);

    StuResume checkResumeSubmitCount(StuSimpleResumeDTO stuSimpleResumeDTO,Long userId);
}
