package com.achobeta.domain.evaluate.service;

import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.evaluate.model.vo.InterviewPaperDetailVO;
import com.achobeta.domain.evaluate.model.vo.InterviewQuestionAverageVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview_question_score(面试题评分关联表)】的数据库操作Service
* @createDate 2024-08-07 02:02:23
*/
public interface InterviewQuestionScoreService extends IService<InterviewQuestionScore> {

    // 查询 ------------------------------------------

    Optional<InterviewQuestionScore> getInterviewQuestionScore(Long interviewId, Long questionId);

    List<InterviewQuestionScore> getQuestionScoresByInterviewId(Long interviewId);

    List<InterviewQuestionAverageVO> getAverageQuestions(List<Long> questionIds);

    /**
     * 查询面试试卷的详情（内部进行校验，调用者无需检查 interviewId）
     * @param interviewId 面试 id
     * @return 试卷的详情、题目的打分情况、每道题历史平均分
     */
    InterviewPaperDetailVO getInterviewPaperDetail(Long interviewId);

    // 写入 ------------------------------------------

    void scoreQuestion(Long interviewId, Long questionId, Integer score);

}
