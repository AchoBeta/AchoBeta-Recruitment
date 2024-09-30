package com.achobeta.domain.message.handler.ext;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;


@Component("text"+ MessageSendWithTextHandler.HANDLER_BASE_NAME)
@RequiredArgsConstructor
public class MessageSendWithTextHandler extends com.achobeta.domain.message.handler.MessageSendHandler {

    private final MessageService messageService;
    @Override
    public void handle(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {
        //校验
        ValidatorUtils.validate(messageSendBody);
        //发送消息
        messageService.sendMessage(messageSendBody.getMessageContent(),webSocketSet);
        //保存消息
        messageService.storeMessage(messageSendBody.getMessageContent());
        super.doNextHandler(messageSendBody,webSocketSet);
    }
}
