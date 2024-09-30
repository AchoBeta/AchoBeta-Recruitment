package com.achobeta.domain.message.converter;


import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface MessageConverter {
    MessageConverter MESSAGE_CONVERTER=Mappers.getMapper(MessageConverter.class);


    MessageContentVO messageContentDTOToVO(MessageContentDTO messageContentBody);

    Message messsageContentDTOToPo(MessageContentDTO messageContent);

    List<MessageContentVO> messageContentPoToVOList(List<Message> messageList);
}
