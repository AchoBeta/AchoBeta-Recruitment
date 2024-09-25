package com.achobeta.domain.message.handler.listener;

import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.domain.message.model.entity.DelayMessage;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.exception.GlobalServiceException;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.achobeta.domain.message.service.MessageService.*;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/22
 */
@Component
@RequiredArgsConstructor
public class MessageHandlerListener {
   /* bindings = @QueueBinding(value =@Queue  (name = MESSAGE_SEND_DEAD_QUEUE, durable = "true"),
    exchange = @Exchange(name = MESSAGE_SEND_DEAD_EXCHANGE,durable = "true"),
    key = {MESSAGE_SEND_DEAD_KEY})*/
    @RabbitListener(queues = MESSAGE_SEND_DEAD_QUEUE)
    public void handleMessage(String messageText) {
        DelayMessage delayMessage= JSON.parseObject(messageText,DelayMessage.class);
        //时间校验
        if(checkTimeIfRight(delayMessage.getMessageSendBody().getMessageContent().getSendTime())){
            String message=String.format("无效的延迟消息->%s",delayMessage.getMessageSendBody().getMessageContent().getContent());
            throw new GlobalServiceException(message);
        }

        //获取消息体各项参数
        MessageSendHandler messageSendHandler = delayMessage.getMessageSendHandler();
        MessageSendDTO messageSendBody = delayMessage.getMessageSendBody();
        CopyOnWriteArraySet<MessageReceiveServer> webSocketSet = delayMessage.getWebSocketSet();

        //责任链消息处理
        Optional.ofNullable(messageSendHandler).ifPresent(handler->handler.handle(messageSendBody,webSocketSet));
    }

    private boolean checkTimeIfRight(LocalDateTime sendTime) {
        return sendTime.isEqual(LocalDateTime.now());
    }
}
