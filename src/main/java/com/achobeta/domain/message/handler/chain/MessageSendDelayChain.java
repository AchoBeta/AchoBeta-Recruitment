package com.achobeta.domain.message.handler.chain;

import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.MessageSendHandlerChain;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.domain.message.model.entity.DelayMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.achobeta.domain.message.service.MessageService.MESSAGE_SEND_KEY;
import static com.achobeta.domain.message.service.MessageService.MESSAGE_SEND_QUEUE;


@Component("delay"+MessageSendHandlerChain.CHAIN_BASE_NAME)
@RequiredArgsConstructor
@Slf4j
public class MessageSendDelayChain extends MessageSendHandlerChain {

   private final RabbitTemplate rabbitTemplate;
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
        //构建延时消息体
        DelayMessage delayMessage = buildDelayMessage(messageSendBody, webSocketSet, messageSendHandler);

        //延时发送消息
        rabbitTemplate.convertAndSend(MESSAGE_SEND_QUEUE,MESSAGE_SEND_KEY,delayMessage,message -> {
            message.getMessageProperties().setDelay(countDelayTime(messageSendBody));
            return message;
        });
        log.info("延时消息发送成功");
    }

    //计算延时时间
    private static int countDelayTime(MessageSendDTO messageSendBody) {
        return (int) Duration.between(LocalDateTime.now(), messageSendBody.getMessageContent().getSendTime()).getSeconds();
    }

    private static DelayMessage buildDelayMessage(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet, MessageSendHandler messageSendHandler) {
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setMessageSendBody(messageSendBody);
        delayMessage.setWebSocketSet(webSocketSet);
        delayMessage.setMessageSendHandler(messageSendHandler);
        return delayMessage;
    }
}
