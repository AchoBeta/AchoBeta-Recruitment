package com.achobeta.domain.message.service;

import com.achobeta.common.base.BasePageResultEntity;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.dto.StuOfMessageVO;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
* @author cattleyuan
* @description 针对表【message(“活动参与”表)】的数据库操作Service
* @createDate 2024-08-11 22:20:17
*/
public interface MessageService extends IService<Message> {
    public static final String MESSAGE_CACHE_NAME="ab:message:";

    BasePageResultEntity<StuOfMessageVO> queryStuListByCondition(QueryStuListDTO queryStuDTO);

    void sendMessage(MessageContentDTO messageContentBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet);

    void storeMessage(MessageContentDTO messageContent);

    List<MessageContentVO> getMessageListofUser();
}
