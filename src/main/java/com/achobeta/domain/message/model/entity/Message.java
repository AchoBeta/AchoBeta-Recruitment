package com.achobeta.domain.message.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * “活动参与”表
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message extends BaseIncrIDEntity implements Serializable {


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
    private Long attachment;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}