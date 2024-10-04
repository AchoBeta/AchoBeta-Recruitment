package com.achobeta.domain.message.model.converter;


import com.achobeta.domain.message.model.dto.AddMessageTemplateDTO;
import com.achobeta.domain.message.model.dto.UpdateMessageTemplateDTO;
import com.achobeta.domain.message.model.entity.MessageTemplate;
import com.achobeta.domain.message.model.vo.MessageTemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface MessageTemplateConverter {
    MessageTemplateConverter MESSAGE_TEMPLATE_CONVERTER=Mappers.getMapper(MessageTemplateConverter.class);

    List<MessageTemplateVO> messageTemplatesToVOList(List<MessageTemplate> messageTemplateList);

    MessageTemplate updateMessageTemplateDTOToPo(UpdateMessageTemplateDTO updateMessageTemplateDTO);

    MessageTemplate addMessageTemplateDTOToPo(AddMessageTemplateDTO addMessageTemplateDTO);

}
