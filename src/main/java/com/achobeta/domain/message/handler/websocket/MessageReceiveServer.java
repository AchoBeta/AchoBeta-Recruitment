package com.achobeta.domain.message.handler.websocket;


import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.MessageSendHandlerChain;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.exception.GlobalServiceException;
import com.alibaba.fastjson.JSON;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author cattleYuan
 * @date 2024/8/21
 */
@Component
@Slf4j
@ServerEndpoint("/message/{id}")
@Getter
public class MessageReceiveServer {

    private Session session;
    private String userId;
    private static final CopyOnWriteArraySet<MessageReceiveServer> webSocketSet=new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id){
        this.session=session;
        this.userId= id;
        webSocketSet.add(this);
        log.info("用户->{}-连接成功！",userId);
    }

    @OnMessage
    public void onMessage(String message){
        MessageSendDTO messageSendDTO= JSON.parseObject(message, MessageSendDTO.class);
        //构造消息发送链路
        MessageSendHandlerChain sendHandlerChain = buildMessageSendHandlerChain(messageSendDTO);
        //消息发送,启动!
        sendHandlerChain.handleChain(messageSendDTO,webSocketSet);

    }

    private static MessageSendHandlerChain buildMessageSendHandlerChain(MessageSendDTO messageSendDTO) {
        //获取消息处理链路
        String chainName = messageSendDTO.getMessageContent().getSendType() + MessageSendHandlerChain.CHAIN_BASE_NAME;
        judgeBeanIfExist(chainName);
        //初始化消息处理链路
        MessageSendHandlerChain sendHandlerChain = (MessageSendHandlerChain) SpringUtil.getBean(chainName);
        messageSendDTO.getSendTypeList().stream().forEach(sendType->{
            //获取要发送的所有消息类型
            String handlerBeanName=sendType+MessageSendHandler.HANDLER_BASE_NAME;
            judgeBeanIfExist(handlerBeanName);
            sendHandlerChain.addHandler((MessageSendHandler)SpringUtil.getBean(handlerBeanName));
        });
        return sendHandlerChain;
    }

    private static void judgeBeanIfExist(String chainName) {
        ListableBeanFactory beanFactory = SpringUtil.getBeanFactory();
        if (!beanFactory.containsBean(chainName)) {
            throw new GlobalServiceException(String.format("ioc 容器未找到 bean:'%s'", chainName));
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        log.info("用户->{}-退出连接",userId);
    }

}
