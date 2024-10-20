package com.achobeta.domain.feedback.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.feedback.model.dto.HandleFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.QueryUserOfFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.UserFeedbackDTO;
import com.achobeta.domain.feedback.model.vo.FeedbackMessageVO;
import com.achobeta.domain.feedback.model.vo.QueryUserOfFeedbackVO;
import com.achobeta.domain.feedback.model.vo.UserPersonalFeedBackVO;
import com.achobeta.domain.feedback.service.UserFeedbackService;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.po.UserHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: cattleyuan
 * @date: 2024/7/10
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class UserFeedbackController {

    private final UserFeedbackService userFeedbackService;

    /**
     * @description: 查询用户个人反馈列表
     * @author: cattleyuan
     * @date: 2024/7/10 15:52
     * @param: []
     * @return: com.achobeta.common.SystemJsonResponse
     **/
    @GetMapping("/query")
    @Intercept(permit = {UserTypeEnum.USER})
    public SystemJsonResponse queryPersonalFeedbackList() {
        long userId = BaseContext.getCurrentUser().getUserId();
        //查询用户个人反馈列表
        List<UserPersonalFeedBackVO> userPersonalFeedBackVOList = userFeedbackService.getUserFeedbackList(userId);

        return SystemJsonResponse.SYSTEM_SUCCESS(userPersonalFeedBackVOList);
    }

    @PostMapping("/submit")
    @Intercept(permit = {UserTypeEnum.USER})
    public SystemJsonResponse submitFeedback(@Valid @RequestBody UserFeedbackDTO userFeedbackDTO) {
        long userId = BaseContext.getCurrentUser().getUserId();
        //提交反馈
        userFeedbackService.submitUserFeedback(userId, userFeedbackDTO);

        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    //管理端查询用户反馈列表
    @PostMapping("/query")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse queryFeedbackofUserList(@Valid @RequestBody QueryUserOfFeedbackDTO queryUserOfFeedbackDTO) {
        //条件分页查询列表
        QueryUserOfFeedbackVO queryUserOfFeedbackVO = userFeedbackService.queryUserOfFeedbackList(queryUserOfFeedbackDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(queryUserOfFeedbackVO);
    }

    @PostMapping("/handle")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse handleFeedbackofUser(@Valid @RequestBody HandleFeedbackDTO handleFeedbackDTO) {
        //处理反馈结果
        Long feedBackId = userFeedbackService.handleFeedbackOfUser(handleFeedbackDTO);

        return SystemJsonResponse.SYSTEM_SUCCESS(feedBackId);
    }

    @GetMapping("/get")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse queryHandleMessage(@RequestParam @NotNull Long messageId) {
        // 获取当前用户
        UserHelper currentUser = BaseContext.getCurrentUser();

        //检查消息是否存在
        Message message = userFeedbackService.judgeMessageOfFeedbackIfExist(messageId);
        //处理反馈结果
        FeedbackMessageVO feedbackMessageVO = userFeedbackService.queryMessageOfFeedback(currentUser, message);

        return SystemJsonResponse.SYSTEM_SUCCESS(feedbackMessageVO);
    }

}

