package com.achobeta.domain.message.handler;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class MessageSendHandlerChain {
    public static final String CHAIN_BASE_NAME="handlerChain";
    public List<MessageSendHandler> messageSendHandlerList;

    public void addHandler(MessageSendHandler messageSendHandler){
        messageSendHandlerList.add(messageSendHandler);
    }

    public abstract void handleChain(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet);
}
