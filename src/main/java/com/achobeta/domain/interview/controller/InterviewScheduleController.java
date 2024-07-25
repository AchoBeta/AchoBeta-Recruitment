package com.achobeta.domain.interview.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.interview.model.dto.ScheduleDTO;
import com.achobeta.domain.interview.service.InterviewScheduleService;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-25
 * Time: 23:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class InterviewScheduleController {

    private final ActivityParticipationService activityParticipationService;

    private final InterviewScheduleService interviewScheduleService;

    @PostMapping("/create")
    public SystemJsonResponse createInterviewSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        // 校验
        ValidatorUtils.validate(scheduleDTO);
        Long managerId = BaseContext.getCurrentUser().getUserId();
        Long participationId = scheduleDTO.getParticipationId();
        activityParticipationService.checkParticipationExists(participationId);
        // 添加
        Long scheduleId = interviewScheduleService.createInterviewSchedule(managerId, participationId,
                scheduleDTO.getStartTime(), scheduleDTO.getEndTime());
        return SystemJsonResponse.SYSTEM_SUCCESS(scheduleId);
    }

}
