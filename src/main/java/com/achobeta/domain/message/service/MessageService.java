package com.achobeta.domain.message.service;

import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.EmailSendDTO;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.model.vo.QueryStuListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
* @author cattleyuan
* @description 针对表【message(“活动参与”表)】的数据库操作Service
* @createDate 2024-08-11 22:20:17
*/
public interface MessageService extends IService<Message> {
    public static final String MESSAGE_CACHE_NAME="ab:message:";

    public static final String MESSAGE_SEND_DEAD_QUEUE="message.send.dead.queue";
    public static final String MESSAGE_SEND_DEAD_EXCHANGE="message.send.dead.exchange";
    public static final String MESSAGE_SEND_QUEUE="message.send.queue";
    public static final String MESSAGE_SEND_EXCHANGE="message.send.exchange";
    public static final String MESSAGE_SEND_DEAD_KEY="delay_send";
    public static final String MESSAGE_SEND_KEY="message_send";


    QueryStuListVO queryStuListByCondition(QueryStuListDTO queryStuDTO);

    void sendMessage(MessageContentDTO messageContentBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet);

    Long storeMessage(MessageContentDTO messageContent);

    List<MessageContentVO> getMessageListofUser();

    void sendMessageByEmail(EmailSendDTO emailSendDTO, List<MultipartFile> attachmentList);

}
