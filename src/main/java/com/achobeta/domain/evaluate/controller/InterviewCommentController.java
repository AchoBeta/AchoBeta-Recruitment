package com.achobeta.domain.evaluate.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.evaluate.model.dto.InterviewCommentDTO;
import com.achobeta.domain.evaluate.model.dto.InterviewCommentUpdateDTO;
import com.achobeta.domain.evaluate.model.vo.InterviewCommentVO;
import com.achobeta.domain.evaluate.service.InterviewCommentService;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import jakarta.validation.Valid;
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
 * Date: 2024-08-07
 * Time: 14:28
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evaluate/comment")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class InterviewCommentController {

    private final InterviewService interviewService;

    private final InterviewCommentService interviewCommentService;

    @PostMapping("/create")
    public SystemJsonResponse commentInterview(@Valid @RequestBody InterviewCommentDTO interviewCommentDTO) {
        Long interviewId = interviewCommentDTO.getInterviewId();
        interviewService.checkInterviewExists(interviewId);
        Long managerId = BaseContext.getCurrentUser().getUserId();
        // 评论
        Long commentId = interviewCommentService.commentInterview(interviewId, managerId, interviewCommentDTO.getContent());
        return SystemJsonResponse.SYSTEM_SUCCESS(commentId);
    }

    @PostMapping("/update")
    public SystemJsonResponse updateComment(@Valid @RequestBody InterviewCommentUpdateDTO interviewCommentUpdateDTO) {
        Long commentId = interviewCommentUpdateDTO.getCommentId();
        Long managerId = interviewCommentService.checkAndGetInterviewCommentExists(commentId).getManagerId();
        if(!managerId.equals(BaseContext.getCurrentUser().getUserId())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        // 更新
        interviewCommentService.updateComment(commentId, interviewCommentUpdateDTO.getContent());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/remove/{commentId}")
    public SystemJsonResponse removeComment(@PathVariable("commentId") @NotNull Long commentId) {
        // 检查
        Long managerId = interviewCommentService.checkAndGetInterviewCommentExists(commentId).getManagerId();
        if(!managerId.equals(BaseContext.getCurrentUser().getUserId())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        // 更新
        interviewCommentService.removeComment(commentId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }


    @GetMapping("/list/{interviewId}")
    public SystemJsonResponse getCommentList(@PathVariable("interviewId") @NotNull Long interviewId) {
        // 检查
        interviewService.checkInterviewExists(interviewId);
        // 查询
        List<InterviewCommentVO> commentList = interviewCommentService.getCommentListByInterviewId(interviewId);
        return SystemJsonResponse.SYSTEM_SUCCESS(commentList);
    }

}
