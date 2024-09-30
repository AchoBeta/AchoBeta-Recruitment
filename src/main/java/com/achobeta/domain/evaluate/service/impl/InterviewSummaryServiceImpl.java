package com.achobeta.domain.evaluate.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.evaluate.constants.InterviewEvaluateConstants;
import com.achobeta.domain.evaluate.model.converter.InterviewSummaryConverter;
import com.achobeta.domain.evaluate.model.dao.mapper.InterviewSummaryMapper;
import com.achobeta.domain.evaluate.model.dto.InterviewSummaryDTO;
import com.achobeta.domain.evaluate.model.entity.InterviewSummary;
import com.achobeta.domain.evaluate.model.vo.InterviewRankVO;
import com.achobeta.domain.evaluate.model.vo.InterviewSummaryVO;
import com.achobeta.domain.evaluate.service.InterviewSummaryService;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview_summary(面试总结表)】的数据库操作Service实现
* @createDate 2024-08-07 01:34:40
*/
@Service
@RequiredArgsConstructor
public class InterviewSummaryServiceImpl extends ServiceImpl<InterviewSummaryMapper, InterviewSummary>
    implements InterviewSummaryService{

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    private final InterviewSummaryMapper interviewSummaryMapper;

    @Override
    public Optional<InterviewSummary> getInterviewSummaryByInterviewId(Long interviewId) {
        return this.lambdaQuery()
                .eq(InterviewSummary::getInterviewId, interviewId)
                .oneOpt();
    }

    @Override
    public InterviewSummaryVO queryInterviewSummaryByInterviewId(Long interviewId) {
        InterviewSummary interviewSummary = getInterviewSummaryByInterviewId(interviewId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_NOT_SUMMARIZED));
        return InterviewSummaryConverter.INSTANCE.interviewSummaryToInterviewSummaryVO(interviewSummary);
    }

    @Override
    public List<InterviewRankVO> getInterviewRankList(InterviewConditionDTO condition) {
        return interviewSummaryMapper.getInterviewRankList(condition);
    }

    @Override
    public void summaryInterview(InterviewSummaryDTO interviewSummaryDTO) {
        // 转化
        Long interviewId = interviewSummaryDTO.getInterviewId();
        redisLock.tryLockDoSomething(InterviewEvaluateConstants.INTERVIEW_SUMMARY_LOCK + interviewId, () -> {
            InterviewSummary interviewSummary =
                    InterviewSummaryConverter.INSTANCE.interviewSummaryDTOToInterviewSummary(interviewSummaryDTO);
            getInterviewSummaryByInterviewId(interviewId).map(InterviewSummary::getId).ifPresentOrElse(summaryId -> {
                this.lambdaUpdate()
                        .eq(InterviewSummary::getId, summaryId)
                        .update(interviewSummary);
            }, () -> {
                this.save(interviewSummary);
            });

        }, () -> {}, simpleLockStrategy);
    }
}




