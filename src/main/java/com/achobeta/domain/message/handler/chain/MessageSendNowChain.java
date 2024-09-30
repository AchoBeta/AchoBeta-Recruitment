package com.achobeta.domain.message.handler.chain;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.MessageSendHandlerChain;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;


@Component("now"+MessageSendHandlerChain.CHAIN_BASE_NAME)
@Slf4j
public class MessageSendNowChain extends MessageSendHandlerChain {

    private MessageSendHandler initHandlerChain() {
        int size = messageSendHandlerList.size();
        if(size == 0) {
            return null;
        }
        for (int i = 0; i < size - 1; i++) {
            messageSendHandlerList.get(i).setNextHandler(messageSendHandlerList.get(i + 1));
        }
        return messageSendHandlerList.getFirst();
    }

    @Override
    public void handleChain(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {
        //初始化责任链
        MessageSendHandler messageSendHandler = initHandlerChain();
        //消息处理
        Optional.ofNullable(messageSendHandler).ifPresent(handler->handler.handle(messageSendBody,webSocketSet));
    }
}
