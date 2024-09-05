package com.achobeta.domain.message.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.message.model.dto.AddMessageTemplateDTO;
import com.achobeta.domain.message.model.dto.UpdateMessageTemplateDTO;
import com.achobeta.domain.message.model.vo.MessageTemplateVO;
import com.achobeta.domain.message.service.MessageTemplateService;
import com.achobeta.util.ValidatorUtils;
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
@RequestMapping("/api/v1/template")
public class MessageTemplateController {

    private final MessageTemplateService messageTemplateService;

    /**
     * @description: 查询模板消息列表
     * @author: cattleyuan
     * @date: 2024/7/10 18:04
     **/
    //查询模板消息列表
    @GetMapping("/query")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse queryMessageTemplateList(){

        //查找模板消息列表
        List<MessageTemplateVO> messageTemplateVOList= messageTemplateService.queryMessageTemplateList();

        return SystemJsonResponse.SYSTEM_SUCCESS(messageTemplateVOList);
    }

    /**
     * @description: 添加模板消息
     * @author: cattleyuan
     * @date: 2024/7/10 18:04
     **/
    @PostMapping("/add")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse addMessageTemplate(@RequestBody AddMessageTemplateDTO addMessageTemplateDTO){
        //校验
        ValidatorUtils.validate(addMessageTemplateDTO);

        //添加模板消息
        messageTemplateService.addMessageTemplate(addMessageTemplateDTO);

        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    /**
     * @description: 根据id删除模板消息
     * @author: cattleyuan
     * @date: 2024/7/10 18:04
     **/
    @DeleteMapping ("/del/{templateId}")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse removeMessageTemplate(@PathVariable("templateId") Long templateId){
        //校验模板消息是否存在
        messageTemplateService.checkMessageTemplateIfExist(templateId);
        //删除模板消息
        messageTemplateService.removeMessageTemplate(templateId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    /**
     * @description: 根据id修改模板消息
     * @author: cattleyuan
     * @date: 2024/7/10 18:04
     **/
    @PutMapping("/update")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse updateMessageTemplate(@RequestBody UpdateMessageTemplateDTO updateMessageTemplateDTO){
        //校验
        ValidatorUtils.validate(updateMessageTemplateDTO);
        //校验模板消息是否存在
        messageTemplateService.checkMessageTemplateIfExist(updateMessageTemplateDTO.getId());
        //更新模板消息
        messageTemplateService.updateMessageTemplate(updateMessageTemplateDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
