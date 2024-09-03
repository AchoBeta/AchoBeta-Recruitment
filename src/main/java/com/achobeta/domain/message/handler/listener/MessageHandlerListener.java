package com.achobeta.domain.message.handler.listener;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.service.MessageService;
import io.netty.channel.pool.ChannelHealthChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/22
 */
@Component
@RequiredArgsConstructor
public class MessageHandlerListener {
    private final MessageService messageService;
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "dead",durable = "true"), exchange = @Exchange(name = "")))
    public void handleMessage(MessageContentDTO messageContentBody, CopyOnWriteArraySet<MessageReceiveServer> websocketSet){
       /* checkTimeIfRight()*/
        messageService.sendMessage(messageContentBody,websocketSet);
        messageService.storeMessage(messageContentBody);
    }
}
