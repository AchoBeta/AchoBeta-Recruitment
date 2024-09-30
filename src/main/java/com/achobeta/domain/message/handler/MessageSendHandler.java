package com.achobeta.domain.message.handler;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;


public abstract class MessageSendHandler {
    public static final String HANDLER_BASE_NAME="handler";
    private MessageSendHandler next;

    public abstract void handle(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet);

    public void setNextHandler(MessageSendHandler handler) {
        this.next = handler;
    }

    protected void doNextHandler(MessageSendDTO messageSendBody,CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {
        if(Objects.nonNull(this.next)) {
            this.next.handle(messageSendBody,webSocketSet);
        }
    }
}
