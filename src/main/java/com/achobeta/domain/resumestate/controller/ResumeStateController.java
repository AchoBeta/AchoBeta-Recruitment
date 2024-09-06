package com.achobeta.domain.resumestate.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.model.converter.ResumeStateConverter;
import com.achobeta.domain.resumestate.model.dto.ResumeExecuteDTO;
import com.achobeta.domain.resumestate.model.vo.ResumeEventVO;
import com.achobeta.domain.resumestate.model.vo.ResumeStatusVO;
import com.achobeta.domain.resumestate.service.ResumeStateService;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 1:23
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumestate")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class ResumeStateController {

    private final StuResumeService stuResumeService;

    private final ResumeStateService resumeStateService;

    @PostMapping("/execute/{resumeId}")
    public SystemJsonResponse executeEvent(@PathVariable("resumeId") @NotNull Long resumeId,
                                           @RequestParam("event") @NotNull Integer event,
                                           @RequestBody(required = false) ResumeExecuteDTO resumeExecuteDTO) {
        // 检查
        StuResume currentResume = stuResumeService.checkAndGetResume(resumeId);
        ResumeEvent resumeEvent = ResumeEvent.get(event);
        // 当前管理员
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 构造上下文对象
        ResumeContext resumeContext = ResumeContext.builder()
                .managerId(managerId)
                .resume(currentResume)
                .executeDTO(resumeExecuteDTO)
                .build();
        // 转变
        ResumeStatus state = resumeStateService.executeResumeEvent(resumeEvent, resumeContext);
        return SystemJsonResponse.SYSTEM_SUCCESS(state);
    }

    @GetMapping("/list/status")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse getResumeStatusList() {
        List<ResumeStatusVO> interviewStatusVOList =
                ResumeStateConverter.INSTANCE.resumeStatusListToResumeStatusVOList(List.of(ResumeStatus.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewStatusVOList);
    }

    @GetMapping("/list/event")
    public SystemJsonResponse getResumeEventList() {
        List<ResumeEventVO> interviewStatusVOList =
                ResumeStateConverter.INSTANCE.resumeEventListToResumeEventVOList(List.of(ResumeEvent.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewStatusVOList);
    }

}
