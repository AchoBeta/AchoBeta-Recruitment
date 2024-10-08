package com.achobeta.domain.schedule.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.schedule.model.dto.ScheduleDTO;
import com.achobeta.domain.schedule.model.dto.ScheduleUpdateDTO;
import com.achobeta.domain.schedule.model.vo.ParticipationDetailVO;
import com.achobeta.domain.schedule.model.vo.ScheduleDetailVO;
import com.achobeta.domain.schedule.model.vo.ScheduleResumeVO;
import com.achobeta.domain.schedule.model.vo.UserSituationVO;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-25
 * Time: 23:13
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class InterviewScheduleController {

    private final ActivityParticipationService activityParticipationService;

    private final InterviewScheduleService interviewScheduleService;

    private final InterviewerService interviewerService;

    private final RecruitmentActivityService recruitmentActivityService;

    @PostMapping("/create")
    public SystemJsonResponse createInterviewSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        // 校验
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
    public SystemJsonResponse updateInterviewSchedule(@Valid @RequestBody ScheduleUpdateDTO scheduleUpdateDTO) {
        // 检测
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
     *
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

    @GetMapping("/print/situations/{actId}")
    public SystemJsonResponse printUserParticipationSituationByActId(@PathVariable("actId") @NotNull Long actId,
                                                                     @RequestParam(name = "level", required = false) Integer level,
                                                                     @RequestParam(name = "synchronous", required = false) Boolean synchronous) {
        // 检测
        RecruitmentActivity activity = recruitmentActivityService.checkAndGetRecruitmentActivity(actId);
        ResourceAccessLevel accessLevel = Optional.ofNullable(level).map(ResourceAccessLevel::get).orElse(ResourceConstants.DEFAULT_EXCEL_ACCESS_LEVEL);
        // 打印表格
        Long managerId = BaseContext.getCurrentUser().getUserId();
        OnlineResourceVO onlineResourceVO = interviewScheduleService.printSituations(managerId, activity, accessLevel, synchronous);
        return SystemJsonResponse.SYSTEM_SUCCESS(onlineResourceVO);
    }

    @GetMapping("/detail/{scheduleId}")
    public SystemJsonResponse getScheduleDetail(@PathVariable("scheduleId") @NotNull Long scheduleId) {
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
