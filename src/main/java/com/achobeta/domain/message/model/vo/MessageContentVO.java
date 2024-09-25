package com.achobeta.domain.message.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/22
 */
@Getter
@Setter
public class MessageContentVO {
    /**
     * 处理结果的消息id
     */
    private Long messageId;
    /**
     * 发送消息的管理员id
     */
    private Long managerId;

    /**
     * 接收消息的用户id
     */
    private Long userId;

    /**
     * 消息标题
     */
    private String tittle;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 附件url
     */
    private long attachment;
}
