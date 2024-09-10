package com.achobeta.domain.feedback.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.feedback.service.UserFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public SystemJsonResponse queryFeedbackList(){
        /*userFeedbackService.getUserFeedbackList();*/
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
