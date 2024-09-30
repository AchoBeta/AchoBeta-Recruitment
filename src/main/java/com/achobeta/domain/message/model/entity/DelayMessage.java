package com.achobeta.domain.message.model.entity;

import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/5
 */
@Getter
@Setter
public class DelayMessage implements Serializable {
    //消息体
    private MessageSendDTO messageSendBody;

    //接收消息用户群
    private CopyOnWriteArraySet<MessageReceiveServer> webSocketSet;

    //消息处理器
    private MessageSendHandler messageSendHandler;
}
