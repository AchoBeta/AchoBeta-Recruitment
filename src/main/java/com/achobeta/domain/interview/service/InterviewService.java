package com.achobeta.domain.interview.service;

import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview(面试表)】的数据库操作Service
* @createDate 2024-08-05 23:45:13
*/
public interface InterviewService extends IService<Interview> {

    // 查询 ------------------------------------------

    Optional<Interview> getInterview(Long interviewId);

    List<InterviewVO> managerGetInterviewList(Long managerId);

    List<InterviewVO> userGetInterviewList(Long userId);

    InterviewDetailVO getInterviewDetail(Long interviewId);

    Long getInterviewPaperId(Long interviewId);

    // 写入 ------------------------------------------

    Long createInterview(InterviewCreateDTO interviewCreateDTO, Long managerId);

    void updateInterview(InterviewUpdateDTO interviewUpdateDTO);

    void switchInterview(Long interviewId, InterviewStatusEnum interviewStatusEnum);

    void setPaperForInterview(Long interviewId, Long paperId);

    // 检测 ------------------------------------------

    void checkInterviewExists(Long interviewId);

    Interview checkAndGetInterviewExists(Long interviewId);

    void checkInterviewStatus(Long interviewId, InterviewStatusEnum interviewStatusEnum);

}
