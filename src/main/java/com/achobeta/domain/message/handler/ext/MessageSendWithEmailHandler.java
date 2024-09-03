package com.achobeta.domain.message.handler.ext;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;


@Component("email"+ MessageSendHandler.HANDLER_BASE_NAME)
@RequiredArgsConstructor
public class MessageSendWithEmailHandler extends MessageSendHandler {

    @Override
    public void handle(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {

        super.doNextHandler(messageSendBody,webSocketSet);
    }
}
