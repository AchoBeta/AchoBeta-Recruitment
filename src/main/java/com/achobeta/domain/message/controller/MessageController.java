package com.achobeta.domain.message.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.message.model.dto.EmailMassDTO;
import com.achobeta.domain.message.model.dto.EmailSendDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.model.vo.QueryStuListVO;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: cattleyuan
 * @date: 2024/8/10
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/stu/query")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse queryStuList(@Valid @RequestBody QueryStuListDTO queryStuDTO) {
        //查找消息发送同学列表
        QueryStuListVO stuListPageResult = messageService.queryStuListByCondition(queryStuDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuListPageResult);
    }

    @GetMapping("/info/query")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse queryMessageListOfUser() {
        List<MessageContentVO> messageContentVOList = messageService.getMessageListOfUser();
        return SystemJsonResponse.SYSTEM_SUCCESS(messageContentVOList);
    }

    @PostMapping("/email/send")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse sendMessageByEmail(@Valid EmailSendDTO emailSendDTO, @RequestPart(value = "attachment", required = false) List<MultipartFile> attachmentList) {
        Long managerId = BaseContext.getCurrentUser().getUserId();
        //发送邮箱
        messageService.sendMessageByEmail(managerId, emailSendDTO, attachmentList);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/email/mass")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse massEmailing(@Valid EmailMassDTO emailMassDTO, @RequestPart(value = "attachment", required = false) List<MultipartFile> attachmentList) {
        Long managerId = BaseContext.getCurrentUser().getUserId();
        //发送邮箱
        messageService.sendMessageByEmail(managerId, emailMassDTO, attachmentList);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
