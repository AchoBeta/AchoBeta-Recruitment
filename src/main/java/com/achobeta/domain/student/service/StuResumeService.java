package com.achobeta.domain.student.service;

import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.dto.StuSimpleResumeDTO;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    void submitResume(StuResumeDTO stuResumeDTO, Long userId);

    StuResumeVO getResumeInfo(QueryResumeDTO queryResumeDTO);

    StuResume checkAndGetResume(Long resumeId);

    StuResume checkResumeSubmitCount(StuSimpleResumeDTO stuSimpleResumeDTO, Long userId);

    List<StuResume> queryStuList(List<Long> userIds);
}
