package com.achobeta.domain.message.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.base.BasePageResultEntity;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.message.model.dto.EmailSendDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.dto.StuOfMessageVO;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.util.ValidatorUtils;
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
    public SystemJsonResponse queryStuList(@RequestBody QueryStuListDTO queryStuDTO){

        //校验
        ValidatorUtils.validate(queryStuDTO);
        //查找消息发送同学列表
        BasePageResultEntity<StuOfMessageVO> stuListPageResult =messageService.queryStuListByCondition(queryStuDTO);

        return SystemJsonResponse.SYSTEM_SUCCESS(stuListPageResult);
    }


    @GetMapping("/info/query")
    @Intercept(permit = {UserTypeEnum.ADMIN,UserTypeEnum.USER})
    public SystemJsonResponse queryMessageListofUser(){

        List<MessageContentVO> messageContentVOList=messageService.getMessageListofUser();

        return SystemJsonResponse.SYSTEM_SUCCESS(messageContentVOList);
    }

    @PostMapping("/email/send")
    @Intercept(permit = {UserTypeEnum.ADMIN,UserTypeEnum.USER})
    public SystemJsonResponse sendMessageByEmail(EmailSendDTO emailSendDTO, @RequestPart(value = "attachment",required = false)List<MultipartFile> attachmentList){

        //校验
        ValidatorUtils.validate(emailSendDTO);
        //发送邮箱
        messageService.sendMessageByEmail(emailSendDTO,attachmentList);

        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
