package com.achobeta.domain.message.service;

import com.achobeta.domain.message.model.dto.AddMessageTemplateDTO;
import com.achobeta.domain.message.model.dto.UpdateMessageTemplateDTO;
import com.achobeta.domain.message.model.entity.MessageTemplate;
import com.achobeta.domain.message.model.vo.MessageTemplateVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86150
* @description 针对表【message_template(模板消息表)】的数据库操作Service
* @createDate 2024-07-10 16:57:24
*/
public interface MessageTemplateService extends IService<MessageTemplate> {

    void addMessageTemplate(AddMessageTemplateDTO addMessageTemplateDTO);

    void removeMessageTemplate(Long templateId);

    void updateMessageTemplate(UpdateMessageTemplateDTO updateMessageTemplateDTO);

    List<MessageTemplateVO> queryMessageTemplateList();
}
