package com.achobeta.domain.interview.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.interview.model.entity.Interviewer;
import com.achobeta.domain.interview.model.vo.ScheduleResumeVO;
import com.achobeta.domain.interview.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.RedisLock;
import com.achobeta.redis.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.entity.InterviewSchedule;
import com.achobeta.domain.interview.service.InterviewScheduleService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewScheduleMapper;
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

    private final InterviewerService interviewerService;

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




