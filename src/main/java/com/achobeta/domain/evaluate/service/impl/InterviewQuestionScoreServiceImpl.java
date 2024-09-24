package com.achobeta.domain.evaluate.service.impl;

import com.achobeta.domain.evaluate.model.converter.InterviewScoreConverter;
import com.achobeta.domain.evaluate.model.dao.mapper.InterviewQuestionScoreMapper;
import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.evaluate.model.vo.InterviewPaperDetailVO;
import com.achobeta.domain.evaluate.model.vo.InterviewQuestionAverageVO;
import com.achobeta.domain.evaluate.service.InterviewQuestionScoreService;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author 马拉圈
* @description 针对表【interview_question_score(面试题评分关联表)】的数据库操作Service实现
* @createDate 2024-08-07 02:02:23
*/
@Service
@RequiredArgsConstructor
public class InterviewQuestionScoreServiceImpl extends ServiceImpl<InterviewQuestionScoreMapper, InterviewQuestionScore>
    implements InterviewQuestionScoreService{

    private final static String QUESTION_SCORE_LOCK = "questionScoreLock:%d:%d";

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    private final InterviewQuestionScoreMapper interviewQuestionScoreMapper;

    private final InterviewService interviewService;

    private final QuestionPaperService questionPaperService;

    private final PaperQuestionLinkService paperQuestionLinkService;

    private String getQuestionScoreLock(Long interviewId, Long questionId) {
        return String.format(QUESTION_SCORE_LOCK, interviewId, questionId);
    }

    @Override
    public Optional<InterviewQuestionScore> getInterviewQuestionScore(Long interviewId, Long questionId) {
        return this.lambdaQuery()
                .eq(InterviewQuestionScore::getInterviewId, interviewId)
                .eq(InterviewQuestionScore::getQuestionId, questionId)
                .oneOpt();
    }

    @Override
    public List<InterviewQuestionScore> getQuestionScoresByInterviewId(Long interviewId) {
        return this.lambdaQuery()
                .eq(InterviewQuestionScore::getInterviewId, interviewId)
                .list();
    }

    @Override
    public List<InterviewQuestionAverageVO> getAverageQuestions(List<Long> questionIds) {
        return Optional.ofNullable(questionIds)
                .map(ids -> interviewQuestionScoreMapper.getAverageQuestions(questionIds))
                .orElse(new ArrayList<>());
    }

    @Override
    public void scoreQuestion(Long interviewId, Long questionId, Integer score) {
        redisLock.tryLockDoSomething(getQuestionScoreLock(interviewId, questionId), () -> {
            getInterviewQuestionScore(interviewId, questionId).map(InterviewQuestionScore::getId).ifPresentOrElse(scoreId -> {
                this.lambdaUpdate()
                        .eq(InterviewQuestionScore::getId, scoreId)
                        .set(InterviewQuestionScore::getScore, score)
                        .update();
            }, () -> {
                InterviewQuestionScore interviewQuestionScore = new InterviewQuestionScore();
                interviewQuestionScore.setInterviewId(interviewId);
                interviewQuestionScore.setQuestionId(questionId);
                interviewQuestionScore.setScore(score);
                this.save(interviewQuestionScore);
            });
        }, () -> {}, simpleLockStrategy);
    }

    @Override
    public InterviewPaperDetailVO getInterviewPaperDetail(Long interviewId) {
        // 获得试卷 id
        Long paperId = interviewService.getInterviewPaperId(interviewId);
        questionPaperService.checkPaperExists(paperId);
        // 获取试卷详情
        QuestionPaperDetailVO paperDetail = paperQuestionLinkService.getPaperDetail(paperId);
        InterviewPaperDetailVO interviewPaperDetailVO =
                InterviewScoreConverter.INSTANCE.questionPaperDetailVOToInterviewPaperDetailVO(paperDetail);
        // 获取面试评分情况
        Map<Long, Integer> scoreMap = getQuestionScoresByInterviewId(interviewId)
                .stream()
                .collect(Collectors.toMap(
                        InterviewQuestionScore::getQuestionId,
                        questionScore -> Optional.ofNullable(questionScore.getScore()).orElse(0),
                        (oldData, newData) -> newData
                ));
        // 获取问题列表
        List<Long> questionIds = paperDetail.getQuestions()
                .stream()
                .map(QuestionVO::getId)
                .toList();
        // 查询平均值
        Map<Long, Double> averageMap = getAverageQuestions(questionIds)
                .stream()
                .collect(Collectors.toMap(
                        InterviewQuestionAverageVO::getId,
                        averageVO -> Optional.ofNullable(averageVO.getAverage()).orElse(0.0),
                        (oldData, newData) -> newData
                ));
        // 填到试卷详情里
        interviewPaperDetailVO.getQuestions()
                .forEach(question -> {
                    Long questionId = question.getId();
                    question.setScore(scoreMap.getOrDefault(questionId, 0));
                    question.setAverage(averageMap.getOrDefault(questionId, 0.0));
                });
        // 返回
        return interviewPaperDetailVO;
    }
}




