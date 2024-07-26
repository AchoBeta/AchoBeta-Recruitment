package com.achobeta.domain.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.schedule.model.entity.Interviewer;
import com.achobeta.domain.schedule.model.vo.*;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.domain.recruit.model.dao.mapper.ActivityParticipationMapper;
import com.achobeta.domain.recruit.model.vo.QuestionAnswerVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.RecruitmentBatchService;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.RedisLock;
import com.achobeta.redis.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.schedule.model.entity.InterviewSchedule;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.schedule.model.dao.mapper.InterviewScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author 马拉圈
* @description 针对表【interview_schedule(面试预约表)】的数据库操作Service实现
* @createDate 2024-07-25 22:34:52
*/
@Service
@RequiredArgsConstructor
public class InterviewScheduleServiceImpl extends ServiceImpl<InterviewScheduleMapper, InterviewSchedule>
    implements InterviewScheduleService{

    private final static Integer MIN_GAP = 30;

    private final static Integer MAX_GAP = 120;

    private final static TimeUnit GAP_UNIT = TimeUnit.MINUTES;

    private final static String SCHEDULE_EXIT_LOCK = "scheduleExitLock:";

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    private final InterviewScheduleMapper interviewScheduleMapper;

    private final ActivityParticipationMapper activityParticipationMapper;

    private final ActivityParticipationService activityParticipationService;

    private final RecruitmentBatchService recruitmentBatchService;

    private final InterviewerService interviewerService;

    private final TimePeriodService timePeriodService;

    private void timePeriodValidate(Long startTime, Long endTime) {
        long gap = endTime - startTime;
        if(startTime.compareTo(System.currentTimeMillis()) < 0 ||
                gap > GAP_UNIT.toMillis(MAX_GAP) || gap < GAP_UNIT.toMillis(MIN_GAP)) {
            throw new GlobalServiceException("时间段非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
    }

    @Override
    public Optional<InterviewSchedule> getInterviewSchedule(Long scheduleId) {
        return this.lambdaQuery()
                .eq(InterviewSchedule::getId, scheduleId)
                .oneOpt();
    }

    @Override
    public List<ScheduleResumeVO> getInterviewScheduleList(Long managerId, Long actId) {
        return interviewScheduleMapper.getInterviewScheduleList(managerId, actId);
    }

    @Override
    public UserSituationVO getSituationsByActId(Long actId) {
        // periodId --> 时间段计数器
        Map<Long, TimePeriodCountVO> countMap = timePeriodService.getTimePeriodsByActId(actId)
                .stream()
                .collect(Collectors.toMap(
                        TimePeriodVO::getId,
                        t -> {
                            TimePeriodCountVO timePeriodCountVO = BeanUtil.copyProperties(t, TimePeriodCountVO.class);
                            timePeriodCountVO.setCount(0);
                            return timePeriodCountVO;
                        },
                        (oldData, newData) -> newData)
                );
        // participationId --> 用户预约情况
        Map<Long, UserParticipationVO> userParticipationVOMap = interviewScheduleMapper.getSituationsByActId(actId)
                .stream()
                .collect(Collectors.toMap(
                        ParticipationScheduleVO::getParticipationId,
                        userParticipationVO -> BeanUtil.copyProperties(userParticipationVO, UserParticipationVO.class),
                        (oldData, newData) -> newData
                ));
        // 查询时间段选择情况
        List<Long> participationIds = userParticipationVOMap.keySet()
                .stream()
                .toList();
        activityParticipationService.getParticipationPeriods(participationIds).stream()
                .filter(participationPeriodVO -> userParticipationVOMap.containsKey(participationPeriodVO.getId()))
                .forEach(participationPeriodVO -> {
                    Long participationId = participationPeriodVO.getId();
                    List<TimePeriodVO> timePeriodVOS = participationPeriodVO.getTimePeriodVOS();
                    userParticipationVOMap.get(participationId).setTimePeriodVOS(timePeriodVOS);
                    // 统计时间段选中次数
                    timePeriodVOS.stream().map(TimePeriodVO::getId).filter(countMap::containsKey).forEach(periodId -> {
                        countMap.get(periodId).increment();
                    });
                });
        // 构造返回值
        List<UserParticipationVO> userParticipationVOS = userParticipationVOMap.values()
                .stream()
                .sorted(Comparator.comparingInt(up -> up.getTimePeriodVOS().size())) // 根据选择时间段排序
                .sorted(Comparator.comparingInt(up -> up.getScheduleVOS().size())) // 没被安排的会被排在前面
                .toList();
        List<TimePeriodCountVO> timePeriodCountVOS = countMap.values()
                .stream()
                .sorted((x1, x2) -> x2.getCount().compareTo(x1.getCount()))
                .toList();
        UserSituationVO userSituationVO = new UserSituationVO();
        userSituationVO.setUserParticipationVOS(userParticipationVOS);
        userSituationVO.setTimePeriodCountVOS(timePeriodCountVOS);
        return userSituationVO;
    }


    @Override
    public ScheduleDetailVO getInterviewScheduleDetail(Long scheduleId) {
        return interviewScheduleMapper.getInterviewerScheduleDetail(scheduleId);
    }

    @Override
    public ParticipationDetailVO getDetailActivityParticipation(Long participationId) {
        return activityParticipationService.getActivityParticipation(participationId).map(activityParticipation -> {
            // 转化
            ParticipationDetailVO participationDetailVO = BeanUtil.copyProperties(activityParticipation, ParticipationDetailVO.class);
            // 获取用户回答的问题
            List<QuestionAnswerVO> questions = activityParticipationMapper.getQuestions(participationId);
            participationDetailVO.setQuestionAnswerVOS(questions);
            // 获取用户选择的时间段
            List<TimePeriodVO> periods = activityParticipationMapper.getPeriods(participationId);
            participationDetailVO.setTimePeriodVOS(periods);
            // 获取学生的简单简历信息
            ParticipationScheduleVO situations = interviewScheduleMapper.getSituationsByParticipationId(participationId);
            participationDetailVO.setSimpleStudentVO(situations.getSimpleStudentVO());
            participationDetailVO.setScheduleVOS(situations.getScheduleVOS());
            return participationDetailVO;
        }).orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.ACTIVITY_PARTICIPATION_NOT_EXISTS));
    }

    @Override
    @Transactional
    public Long createInterviewSchedule(Long managerId, Long participationId, Long startTime, Long endTime) {
        // 校验时间段
        timePeriodValidate(startTime, endTime);
        // 构造对象
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        interviewSchedule.setParticipationId(participationId);
        interviewSchedule.setStartTime(new Date(startTime));
        interviewSchedule.setEndTime(new Date(endTime));
        // 落库
        this.save(interviewSchedule);
        Long scheduleId = interviewSchedule.getId();
        // 与管理员绑定
        interviewerService.createInterviewer(managerId, scheduleId);
        return scheduleId;
    }

    @Override
    @Transactional
    public void removeInterviewSchedule(Long scheduleId) {
        this.lambdaUpdate()
                .eq(InterviewSchedule::getId, scheduleId)
                .remove();
        interviewerService.removeInterviewersByScheduleId(scheduleId);
    }

    @Override
    @Transactional
    public void exitInterview(Long managerId, Long scheduleId) {
        redisLock.tryLockDoSomething(SCHEDULE_EXIT_LOCK + scheduleId, () -> {
            Set<Long> managerIds = interviewerService.getInterviewersByScheduleId(scheduleId)
                    .stream()
                    .map(Interviewer::getManagerId)
                    .collect(Collectors.toSet());
            int size = managerIds.size();
            if(managerIds.contains(managerId)) {
                interviewerService.removeInterviewer(managerId, scheduleId);
                size--;
            }
            // 若面试预约没有管理员则删除
            if(0 == size) {
                this.removeInterviewSchedule(scheduleId);
            }
        }, () -> {}, simpleLockStrategy);
    }

    @Override
    public void updateInterviewSchedule(Long scheduleId, Long startTime, Long endTime) {
        // 校验时间段
        timePeriodValidate(startTime, endTime);
        // 更新
        this.lambdaUpdate()
                .eq(InterviewSchedule::getId, scheduleId)
                .set(InterviewSchedule::getStartTime, new Date(startTime))
                .set(InterviewSchedule::getEndTime, new Date(endTime))
                .update();
    }

    @Override
    public void checkInterviewScheduleExists(Long scheduleId) {
        getInterviewSchedule(scheduleId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_SCHEDULE_NOT_EXISTS));
    }
}




