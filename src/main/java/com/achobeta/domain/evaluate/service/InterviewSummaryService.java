package com.achobeta.domain.evaluate.service;

import com.achobeta.domain.evaluate.model.dto.InterviewSummaryDTO;
import com.achobeta.domain.evaluate.model.entity.InterviewSummary;
import com.achobeta.domain.evaluate.model.vo.InterviewRankVO;
import com.achobeta.domain.evaluate.model.vo.InterviewSummaryVO;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview_summary(面试总结表)】的数据库操作Service
* @createDate 2024-08-07 01:34:40
*/
public interface InterviewSummaryService extends IService<InterviewSummary> {

    // 查询 ------------------------------------------

    Optional<InterviewSummary> getInterviewSummaryByInterviewId(Long interviewId);

    InterviewSummaryVO queryInterviewSummaryByInterviewId(Long interviewId);

    List<InterviewRankVO> getInterviewRankList(InterviewConditionDTO condition);

    // 写入 ------------------------------------------

    void summaryInterview(InterviewSummaryDTO interviewSummaryDTO);

}
