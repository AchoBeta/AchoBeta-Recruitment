package com.achobeta.domain.interview.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.model.converter.InterviewConverter;
import com.achobeta.domain.interview.model.dto.*;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewEventVO;
import com.achobeta.domain.interview.model.vo.InterviewStatusVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.exception.GlobalServiceException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.achobeta.domain.interview.enums.InterviewStatus.ENDED;
import static com.achobeta.domain.interview.enums.InterviewStatus.NOT_STARTED;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 0:49
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Intercept(permit = {UserTypeEnum.ADMIN})
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;

    private final QuestionPaperService questionPaperService;

    private final InterviewScheduleService interviewScheduleService;

    @PostMapping("/create")
    public SystemJsonResponse createInterview(@Valid @RequestBody InterviewCreateDTO interviewCreateDTO) {
        // 获取当前管理员 id
        Long managerId = BaseContext.getCurrentUser().getUserId();
        interviewScheduleService.checkInterviewScheduleExists(interviewCreateDTO.getScheduleId());
        // 创建
        Long interviewId = interviewService.createInterview(interviewCreateDTO, managerId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewId);
    }

    @PostMapping("/update")
    public SystemJsonResponse updateInterview(@Valid @RequestBody InterviewUpdateDTO interviewUpdateDTO) {
        // 未开始才能修改
        interviewService.checkInterviewStatus(interviewUpdateDTO.getInterviewId(), List.of(NOT_STARTED, ENDED));
        // 更新
        interviewService.updateInterview(interviewUpdateDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/status")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse getInterviewStatusList() {
        List<InterviewStatusVO> interviewStatusVOList =
                InterviewConverter.INSTANCE.interviewStatusListToInterviewStatusVOList(List.of(InterviewStatus.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewStatusVOList);
    }

    @GetMapping("/list/event")
    public SystemJsonResponse getInterviewEventList() {
        List<InterviewEventVO> interviewEventVOList =
                InterviewConverter.INSTANCE.interviewEventListToInterviewEventVOList(List.of(InterviewEvent.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewEventVOList);
    }

    @PostMapping("/execute/{interviewId}")
    public SystemJsonResponse executeInterviewStateEvent(@PathVariable("interviewId") @NotNull Long interviewId,
                                                         @RequestParam("event") @NotNull Integer event,
                                                         @Valid @RequestBody(required = false) InterviewExecuteDTO interviewExecuteDTO) {
        // 检查
        Interview currentInterview = interviewService.checkAndGetInterviewExists(interviewId);
        InterviewEvent interviewEvent = InterviewEvent.get(event);
        // 当前管理员
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 构造上下文
        InterviewContext interviewContext = InterviewContext.builder()
                .managerId(managerId)
                .interview(currentInterview)
                .executeDTO(interviewExecuteDTO)
                .build();
        // 转变
        InterviewStatus state = interviewService.executeInterviewStateEvent(interviewEvent, interviewContext);
        return SystemJsonResponse.SYSTEM_SUCCESS(state);
    }

    @PostMapping("/set/paper")
    public SystemJsonResponse setPaperForInterview(@Valid @RequestBody InterviewPaperDTO interviewPaperDTO) {
        // 检查
        Long interviewId = interviewPaperDTO.getInterviewId();
        Interview interview = interviewService.checkAndGetInterviewExists(interviewId);
        // 检查面试是否未开始
        interview.getStatus().check(List.of(NOT_STARTED, ENDED));
        Long paperId = interviewPaperDTO.getPaperId();
        if(!Objects.equals(interview.getPaperId(), paperId)) {
            // 检查试卷是否存在
            questionPaperService.checkPaperExists(paperId);
            // 设置试卷
            interviewService.setPaperForInterview(interviewId, paperId);
        }
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/list/manager/all")
    public SystemJsonResponse managerGetAllInterviewList(@Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 查询
        List<InterviewDetailVO> interviewVOList = interviewService.managerGetInterviewList(null, InterviewConditionDTO.of(interviewConditionDTO));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewVOList);
    }

    @PostMapping("/print/all")
    public SystemJsonResponse printAllInterviewList(@RequestParam(name = "level", required = false) Integer level,
                                                    @RequestParam(name = "synchronous", required = false) Boolean synchronous,
                                                    @Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 获取当前管理员 id
        Long managerId = BaseContext.getCurrentUser().getUserId();
        ResourceAccessLevel accessLevel = Optional.ofNullable(level).map(ResourceAccessLevel::get).orElse(ResourceConstants.DEFAULT_EXCEL_ACCESS_LEVEL);
        // 打印成表格
        OnlineResourceVO onlineResourceVO = interviewService.printAllInterviewList(managerId, InterviewConditionDTO.of(interviewConditionDTO), accessLevel, synchronous);
        // 构造 url 并返回
        return SystemJsonResponse.SYSTEM_SUCCESS(onlineResourceVO);
    }

    @PostMapping("/list/manager/own")
    public SystemJsonResponse managerGetOwnInterviewList(@Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 获取当前管理员 id
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 查询
        List<InterviewDetailVO> interviewVOList = interviewService.managerGetInterviewList(managerId, InterviewConditionDTO.of(interviewConditionDTO));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewVOList);
    }

    @PostMapping("/list/user")
    @Intercept(permit = {UserTypeEnum.USER})
    public SystemJsonResponse userGetOwnInterviewList(@Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 获取当前用户 id
        Long userId = BaseContext.getCurrentUser().getUserId();
        // 查询
        List<InterviewVO> interviewVOList = interviewService.userGetInterviewList(userId, InterviewConditionDTO.of(interviewConditionDTO));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewVOList);
    }

    @GetMapping("/detail/{interviewId}")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse getInterviewDetail(@PathVariable("interviewId") @NotNull Long interviewId) {
        // 检查
        interviewService.checkInterviewExists(interviewId);
        // 获取当前用户
        UserHelper currentUser = BaseContext.getCurrentUser();
        // 查询
        InterviewDetailVO interviewDetail = interviewService.getInterviewDetail(interviewId);
        if(UserTypeEnum.USER.getCode().equals(currentUser.getRole())) {
            // 判断是否是当前用户的面试
            if(!Objects.equals(currentUser.getUserId(), interviewDetail.getSimpleStudentVO().getUserId())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
            }
            // 对于普通用户，隐藏一些字段
            interviewDetail.hidden();
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewDetail);
    }

}
