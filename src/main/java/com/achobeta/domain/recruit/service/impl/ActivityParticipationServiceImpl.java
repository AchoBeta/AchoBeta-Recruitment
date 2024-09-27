package com.achobeta.domain.recruit.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruit.constants.RecruitmentActivityConstants;
import com.achobeta.domain.recruit.model.converter.ParticipationConverter;
import com.achobeta.domain.recruit.model.dao.mapper.ActivityParticipationMapper;
import com.achobeta.domain.recruit.model.dto.QuestionAnswerDTO;
import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.vo.ParticipationPeriodVO;
import com.achobeta.domain.recruit.model.vo.ParticipationVO;
import com.achobeta.domain.recruit.model.vo.QuestionAnswerVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.ParticipationPeriodLinkService;
import com.achobeta.domain.recruit.service.ParticipationQuestionLinkService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.ReadLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【activity_participation(“活动参与”表)】的数据库操作Service实现
* @createDate 2024-07-06 12:33:02
*/
@Service
@RequiredArgsConstructor
public class ActivityParticipationServiceImpl extends ServiceImpl<ActivityParticipationMapper, ActivityParticipation>
    implements ActivityParticipationService{

    private final RecruitmentActivityService recruitmentActivityService;

    private final ActivityParticipationMapper activityParticipationMapper;

    private final ParticipationPeriodLinkService participationPeriodLinkService;

    private final ParticipationQuestionLinkService participationQuestionLinkService;

    private final RedisLock redisLock;

    private final ReadLockStrategy readLockStrategy;

    @Override
    public Optional<ActivityParticipation> getActivityParticipation(Long participationId) {
        return this.lambdaQuery()
                .eq(ActivityParticipation::getId, participationId)
                .oneOpt();
    }

    @Override
    public ParticipationVO getActivityParticipation(Long stuId, Long actId) {
        // 如果未启动且之前没有参与，则不会创建一份新的
        return this.lambdaQuery()
                .eq(ActivityParticipation::getStuId, stuId)
                .eq(ActivityParticipation::getActId, actId)
                .oneOpt()
                .map(activityParticipation -> {
                    Long participationId = activityParticipation.getId();
                    // 转化
                    ParticipationVO participationUserVO =
                            ParticipationConverter.INSTANCE.activityParticipationToParticipationVO(activityParticipation);
                    // 获取用户回答的问题
                    List<QuestionAnswerVO> questions = activityParticipationMapper.getQuestions(participationId);
                    participationUserVO.setQuestionAnswerVOS(questions);
                    // 获取用户选择的时间段
                    List<TimePeriodVO> periods = activityParticipationMapper.getPeriods(participationId);
                    participationUserVO.setTimePeriodVOS(periods);
                    return participationUserVO;
                }).orElseGet(() -> createActivityParticipation(stuId, actId));
    }

    @Override
    public List<Long> getStuIdsByActId(Long actId) {
        return this.lambdaQuery()
                .eq(ActivityParticipation::getActId, actId)
                .list()
                .stream()
                .map(ActivityParticipation::getStuId)
                .toList();
    }

    @Override
    public List<Long> getParticipationIdsByPaperId(Long paperId) {
        return recruitmentActivityService.getActIdsByPaperId(paperId)
                .stream()
                .map(recruitmentActivityService::getParticipationIdsByActId)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public List<ParticipationPeriodVO> getParticipationPeriods(List<Long> participationIds) {
        if(participationIds.isEmpty()) {
            return new ArrayList<>();
        }
        return activityParticipationMapper.getParticipationPeriods(participationIds);
    }

    @Override
    public ParticipationVO createActivityParticipation(Long stuId, Long actId) {
        // 判断活动是否开始
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.TRUE);
        ActivityParticipation activityParticipation = new ActivityParticipation();
        activityParticipation.setStuId(stuId);
        activityParticipation.setActId(actId);
        this.save(activityParticipation);
        ParticipationVO participationUserVO =
                ParticipationConverter.INSTANCE.activityParticipationToParticipationVO(activityParticipation);
        participationUserVO.setQuestionAnswerVOS(new ArrayList<>());
        participationUserVO.setTimePeriodVOS(new ArrayList<>());
        return participationUserVO;
    }

    @Override
    @Transactional
    public void participateInActivity(ActivityParticipation activityParticipation, List<QuestionAnswerDTO> questionAnswerVOS, List<Long> periodIds) {
        Long actId = activityParticipation.getActId();
        // 读锁保证读到的数据在过程中准确
        redisLock.tryLockDoSomething(RecruitmentActivityConstants.RECRUITMENT_ACTIVITY_QUESTIONNAIRE_LOCK + actId, () -> {
            Long participationId = activityParticipation.getId();
            Long stuId = activityParticipation.getStuId();
            // 检测用户是否是活动面向的对象
            recruitmentActivityService.checkCanUserParticipateInActivity(stuId, actId);
            // 更新问题的回答
            participationQuestionLinkService.putQuestionAnswers(participationId, questionAnswerVOS);
            // 更新时间段的选择
            participationPeriodLinkService.putTimePeriods(participationId, periodIds);
        }, () -> {}, readLockStrategy);
    }

    @Override
    public void checkParticipationExists(Long participationId) {
        getActivityParticipation(participationId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.ACTIVITY_PARTICIPATION_NOT_EXISTS));
    }

    @Override
    public ActivityParticipation checkAndGetActivityParticipation(Long participationId) {
        return getActivityParticipation(participationId)
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.ACTIVITY_PARTICIPATION_NOT_EXISTS));
    }
}




