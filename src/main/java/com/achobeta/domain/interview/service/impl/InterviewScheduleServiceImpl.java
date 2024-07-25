package com.achobeta.domain.interview.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.interview.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.entity.InterviewSchedule;
import com.achobeta.domain.interview.service.InterviewScheduleService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    private final InterviewerService interviewerService;

    private void timePeriodValidate(Long startTime, Long endTime) {
        long gap = endTime - startTime;
        if(startTime.compareTo(System.currentTimeMillis()) < 0 ||
                gap > GAP_UNIT.toMillis(MAX_GAP) || gap < GAP_UNIT.toMillis(MIN_GAP)) {
            throw new GlobalServiceException("时间段非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
    }

    @Override
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
}




