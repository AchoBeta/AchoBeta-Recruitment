package com.achobeta.domain.schedule.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.model.vo.InterviewReserveVO;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.schedule.model.dto.ScheduleDTO;
import com.achobeta.domain.schedule.model.dto.ScheduleUpdateDTO;
import com.achobeta.domain.schedule.model.dto.SituationQueryDTO;
import com.achobeta.domain.schedule.model.vo.ParticipationDetailVO;
import com.achobeta.domain.schedule.model.vo.ScheduleDetailVO;
import com.achobeta.domain.schedule.model.vo.UserSituationVO;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    @PostMapping("/list/own")
    public SystemJsonResponse getInterviewScheduleList(@Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 检测
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 查询
        List<ScheduleDetailVO> interviewScheduleList = interviewScheduleService.getInterviewScheduleList(managerId, InterviewConditionDTO.of(interviewConditionDTO));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewScheduleList);
    }

    @PostMapping("/list/all")
    public SystemJsonResponse getInterviewScheduleListAll(@Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 查询
        List<ScheduleDetailVO> interviewScheduleList = interviewScheduleService.getInterviewScheduleList(null, InterviewConditionDTO.of(interviewConditionDTO));
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
        UserSituationVO situations = interviewScheduleService.querySituations(actId);
        return SystemJsonResponse.SYSTEM_SUCCESS(situations);
    }

    @PostMapping("/situations")
    public SystemJsonResponse querySituations(@Valid @RequestBody SituationQueryDTO situationQueryDTO) {
        // 检测
        recruitmentActivityService.checkRecruitmentActivityExists(situationQueryDTO.getActId());
        List<Integer> statusList = Optional.ofNullable(situationQueryDTO.getStatusList()).stream().flatMap(Collection::stream).filter(Objects::nonNull).toList();
        situationQueryDTO.setStatusList(statusList);
        // 获取参与本次招新活动的所有用户参与和预约情况
        UserSituationVO situations = interviewScheduleService.querySituations(situationQueryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(situations);
    }

    @GetMapping("/print/situations/{actId}")
    public SystemJsonResponse printUserParticipationSituationByActId(@PathVariable("actId") @NotNull Long actId,
                                                                     @RequestParam(name = "level", required = false) Integer level,
                                                                     @RequestParam(name = "synchronous", required = false) Boolean synchronous) {
        // 检测
        ResourceAccessLevel accessLevel = Optional.ofNullable(level).map(ResourceAccessLevel::get).orElse(ResourceConstants.DEFAULT_EXCEL_ACCESS_LEVEL);
        // 打印表格
        Long managerId = BaseContext.getCurrentUser().getUserId();
        OnlineResourceVO onlineResourceVO = interviewScheduleService.printSituations(managerId, actId, accessLevel, synchronous);
        return SystemJsonResponse.SYSTEM_SUCCESS(onlineResourceVO);
    }

    @PostMapping("/print/situations")
    public SystemJsonResponse printUserParticipationSituations(@Valid @RequestBody SituationQueryDTO situationQueryDTO,
                                                               @RequestParam(name = "level", required = false) Integer level,
                                                               @RequestParam(name = "synchronous", required = false) Boolean synchronous) {
        // 检测
        ResourceAccessLevel accessLevel = Optional.ofNullable(level).map(ResourceAccessLevel::get).orElse(ResourceConstants.DEFAULT_EXCEL_ACCESS_LEVEL);
        // 打印表格
        Long managerId = BaseContext.getCurrentUser().getUserId();
        OnlineResourceVO onlineResourceVO = interviewScheduleService.printSituations(managerId, situationQueryDTO, accessLevel, synchronous);
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

    @GetMapping("/reserve/{scheduleId}")
    public SystemJsonResponse interviewReserveApply(@PathVariable("scheduleId") @NotNull Long scheduleId,
                                                    @RequestParam("title") @NotBlank(message = "标题不能为空") String title,
                                                    @RequestParam(name = "mobile", required = false) @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号非法") String mobile) {
        // 检查
        interviewScheduleService.checkInterviewScheduleExists(scheduleId);
        // 当前管理员
        Long managerId = BaseContext.getCurrentUser().getUserId();
        log.warn("管理员 {} 尝试预约面试 {} {}", managerId, scheduleId, title);
        // 预约会议
        InterviewReserveVO interviewReserveVO = interviewScheduleService.interviewReserveApply(scheduleId, title, mobile);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewReserveVO);
    }

}
