package com.achobeta.domain.message.converter;


import com.achobeta.domain.message.model.dto.AddMessageTemplateDTO;
import com.achobeta.domain.message.model.dto.UpdateMessageTemplateDTO;
import com.achobeta.domain.message.model.entity.MessageTemplate;
import com.achobeta.domain.message.model.vo.MessageTemplateVO;
import com.achobeta.domain.student.model.dto.StuAttachmentDTO;
import com.achobeta.domain.student.model.dto.StuSimpleResumeDTO;
import com.achobeta.domain.student.model.entity.StuAttachment;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuAttachmentVO;
import com.achobeta.domain.student.model.vo.StuSimpleResumeVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface MessageTemplateConverter {
    MessageTemplateConverter STU_RESUME_CONVERTER=Mappers.getMapper(MessageTemplateConverter.class);

    List<MessageTemplateVO> messageTemplatesToVOList(List<MessageTemplate> messageTemplateList);

    MessageTemplate updateMessageTemplateDTOToPo(UpdateMessageTemplateDTO updateMessageTemplateDTO);

    MessageTemplate addMessageTemplateDTOToPo(AddMessageTemplateDTO addMessageTemplateDTO);

}
