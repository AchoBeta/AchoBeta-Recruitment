package com.achobeta.domain.evaluate.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.evaluate.model.dto.InterviewSummaryDTO;
import com.achobeta.domain.evaluate.model.vo.InterviewRankVO;
import com.achobeta.domain.evaluate.model.vo.InterviewSummaryVO;
import com.achobeta.domain.evaluate.service.InterviewSummaryService;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.service.InterviewService;
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

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:10
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evaluate/summary")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class InterviewSummaryController {

    private final InterviewService interviewService;

    private final InterviewSummaryService interviewSummaryService;

    @PostMapping("/mark")
    public SystemJsonResponse summaryInterview(@Valid @RequestBody InterviewSummaryDTO interviewSummaryDTO) {
        // 总结
        interviewSummaryService.summaryInterview(interviewSummaryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/query/{interviewId}")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse queryInterviewSummary(@PathVariable("interviewId") @NotNull Long interviewId) {
        // 检查
        interviewService.checkInterviewExists(interviewId);
        // 查询
        InterviewSummaryVO interviewSummaryVO = interviewSummaryService.queryInterviewSummaryByInterviewId(interviewId);
        // 如果当前用户是普通用户，则判断是否可以查询活动的总结
        UserHelper currentUser = BaseContext.getCurrentUser();
        if(UserTypeEnum.USER.getCode().equals(currentUser.getRole())) {
            Long stuId = interviewService.getInterviewDetail(interviewId).getSimpleStudentVO().getUserId();
            if(!Objects.equals(stuId, currentUser.getUserId())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
            }
            // 隐藏一些字段
            interviewSummaryVO.hidden();
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewSummaryVO);
    }

    @PostMapping("/rank")
    public SystemJsonResponse rankInterview(@Valid @RequestBody(required = false) InterviewConditionDTO interviewConditionDTO) {
        // 查询
        List<InterviewRankVO> interviewRankList = interviewSummaryService.getInterviewRankList(InterviewConditionDTO.of(interviewConditionDTO));
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewRankList);
    }

}
