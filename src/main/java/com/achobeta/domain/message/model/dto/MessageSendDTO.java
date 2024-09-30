package com.achobeta.domain.message.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/2
 */
@Getter
@Setter
public class MessageSendDTO implements Serializable {

    /**
     * 消息发送内容
     */
    private MessageContentDTO messageContent;

    /**
     * 消息发送类型集合(邮件，短信)
     */
    private List<String> sendTypeList;
}
