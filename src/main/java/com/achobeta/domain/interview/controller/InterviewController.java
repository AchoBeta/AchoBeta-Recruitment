package com.achobeta.domain.interview.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewPaperDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.enity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 0:49
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;

    private final QuestionPaperService questionPaperService;

    @PostMapping("/create")
    public SystemJsonResponse createInterview(@RequestBody InterviewCreateDTO interviewCreateDTO) {
        // 检查
        ValidatorUtils.validate(interviewCreateDTO);
        // 获取当前管理员 id
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 创建
        Long interviewId = interviewService.createInterview(interviewCreateDTO, managerId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewId);
    }

    @PostMapping("/update")
    public SystemJsonResponse updateInterview(@RequestBody InterviewUpdateDTO interviewUpdateDTO) {
        // 检查
        ValidatorUtils.validate(interviewUpdateDTO);
        // 未开始才能修改
        interviewService.checkInterviewStatus(interviewUpdateDTO.getInterviewId(), InterviewStatusEnum.NOT_STARTED);
        // 更新
        interviewService.updateInterview(interviewUpdateDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/switch/{interviewId}")
    public SystemJsonResponse switchInterview(@PathVariable("interviewId") @NotNull Long interviewId,
                                              @RequestParam("status") @NotNull Integer status) {
        // 检查
        interviewService.checkInterviewExists(interviewId);
        InterviewStatusEnum interviewStatusEnum = InterviewStatusEnum.get(status);
        // 转变
        interviewService.switchInterview(interviewId, interviewStatusEnum);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/set/paper")
    public SystemJsonResponse setPaperForInterview(@RequestBody InterviewPaperDTO interviewPaperDTO) {
        // 检查
        ValidatorUtils.validate(interviewPaperDTO);
        Long interviewId = interviewPaperDTO.getInterviewId();
        Interview interview = interviewService.checkAndGetInterviewExists(interviewId);
        // 检查面试是否未开始
        interview.getStatus().check(InterviewStatusEnum.NOT_STARTED);
        Long paperId = interviewPaperDTO.getPaperId();
        if(!Objects.equals(interview.getPaperId(), paperId)) {
            // 检查试卷是否存在
            questionPaperService.checkPaperExists(paperId);
            // 设置试卷
            interviewService.setPaperForInterview(interviewId, paperId);
        }
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/manager/all")
    public SystemJsonResponse managerGetAllInterviewList() {
        // 查询
        List<InterviewVO> interviewVOList = interviewService.managerGetInterviewList(null);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewVOList);
    }

    @GetMapping("/list/manager/own")
    public SystemJsonResponse managerGetOwnInterviewList() {
        // 获取当前管理员 id
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 查询
        List<InterviewVO> interviewVOList = interviewService.managerGetInterviewList(managerId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewVOList);
    }

    @GetMapping("/list/user")
    public SystemJsonResponse userGetOwnInterviewList() {
        // 获取当前用户 id
        Long userId = BaseContext.getCurrentUser().getUserId();
        // 查询
        List<InterviewVO> interviewVOList = interviewService.userGetInterviewList(userId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewVOList);
    }

    @GetMapping("/detail/{interviewId}")
    public SystemJsonResponse getInterviewDetail(@PathVariable("interviewId") @NotNull Long interviewId) {
        // 获取当前用户
        UserHelper currentUser = BaseContext.getCurrentUser();
        // 查询
        InterviewDetailVO interviewDetail = interviewService.getInterviewDetail(interviewId);
        if(UserTypeEnum.USER.getCode().equals(currentUser.getRole())) {
            // 判断是否是当前用户的面试
            if(!Objects.equals(currentUser.getUserId(), interviewDetail.getStuId())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
            }
            // 对于普通用户，隐藏一些字段
            interviewDetail.hidden();
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewDetail);
    }

}
