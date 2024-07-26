package com.achobeta.domain.interview.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.interview.model.dto.ScheduleDTO;
import com.achobeta.domain.interview.model.dto.ScheduleUpdateDTO;
import com.achobeta.domain.interview.model.vo.ParticipationDetailVO;
import com.achobeta.domain.interview.model.vo.ScheduleDetailVO;
import com.achobeta.domain.interview.model.vo.ScheduleResumeVO;
import com.achobeta.domain.interview.model.vo.UserSituationVO;
import com.achobeta.domain.interview.service.InterviewScheduleService;
import com.achobeta.domain.interview.service.InterviewerService;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private final InterviewerService interviewerService;

    private final RecruitmentActivityService recruitmentActivityService;

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

    @GetMapping("/remove/{scheduleId}")
    public SystemJsonResponse removeInterviewSchedule(@PathVariable("scheduleId") @NotNull Long scheduleId) {
        // 检测
        Long managerId = BaseContext.getCurrentUser().getUserId();
        interviewScheduleService.checkInterviewScheduleExists(scheduleId);
        interviewerService.checkInterviewerExists(managerId, scheduleId);
        // 删除
        interviewScheduleService.removeInterviewSchedule(scheduleId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/update")
    public SystemJsonResponse updateInterviewSchedule(@RequestBody ScheduleUpdateDTO scheduleUpdateDTO) {
        // 检测
        ValidatorUtils.validate(scheduleUpdateDTO);
        Long managerId = BaseContext.getCurrentUser().getUserId();
        Long scheduleId = scheduleUpdateDTO.getScheduleId();
        interviewScheduleService.checkInterviewScheduleExists(scheduleId);
        interviewerService.checkInterviewerExists(managerId, scheduleId);
        // 删除
        interviewScheduleService.updateInterviewSchedule(scheduleId,
                scheduleUpdateDTO.getStartTime(), scheduleUpdateDTO.getEndTime());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/attend/{scheduleId}")
    public SystemJsonResponse attendSchedule(@PathVariable("scheduleId") @NotNull Long scheduleId) {
        // 检测
        interviewScheduleService.checkInterviewScheduleExists(scheduleId);
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 添加（如果存在则返回对应面试官的 id）
        Long interviewerId = interviewerService.createInterviewer(managerId, scheduleId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewerId);
    }

    @GetMapping("/exit/{scheduleId}")
    public SystemJsonResponse exitInterview(@PathVariable("scheduleId") @NotNull Long scheduleId) {
        // 检测
        interviewScheduleService.checkInterviewScheduleExists(scheduleId);
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 退出
        interviewScheduleService.exitInterview(managerId, scheduleId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/{actId}")
    public SystemJsonResponse getInterviewScheduleList(@PathVariable("actId") @NotNull Long actId) {
        // 检测
        Long managerId = BaseContext.getCurrentUser().getUserId();
        recruitmentActivityService.checkRecruitmentActivityExists(actId);
        // 查询
        List<ScheduleResumeVO> interviewScheduleList = interviewScheduleService.getInterviewScheduleList(managerId, actId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewScheduleList);
    }

    /**
     * 管理员查看用户参与和预约情况
     * @param actId
     * @return
     */
    @GetMapping("/situations/{actId}")
    public SystemJsonResponse getUserParticipationSituationByActId(@PathVariable("actId") @NotNull Long actId) {
        // 检测
        recruitmentActivityService.checkRecruitmentActivityExists(actId);
        // 获取参与本次招新活动的所有用户参与和预约情况
        UserSituationVO situations = interviewScheduleService.getSituationsByActId(actId);
        return SystemJsonResponse.SYSTEM_SUCCESS(situations);
    }

    @GetMapping("/detail/{scheduleId}")
    public SystemJsonResponse getScheduleDetail(@PathVariable("participationId") @NotNull Long scheduleId) {
        // 检测
        interviewScheduleService.checkInterviewScheduleExists(scheduleId);
        // 获取详细信息
        ScheduleDetailVO detail = interviewScheduleService.getInterviewScheduleDetail(scheduleId);
        return SystemJsonResponse.SYSTEM_SUCCESS(detail);
    }

    @GetMapping("/situation/detail/{participationId}")
    public SystemJsonResponse getParticipationDetail(@PathVariable("participationId") @NotNull Long participationId) {
        // 检测
        activityParticipationService.checkParticipationExists(participationId);
        // 获取详细信息
        ParticipationDetailVO detail = interviewScheduleService.getDetailActivityParticipation(participationId);
        return SystemJsonResponse.SYSTEM_SUCCESS(detail);
    }

}
