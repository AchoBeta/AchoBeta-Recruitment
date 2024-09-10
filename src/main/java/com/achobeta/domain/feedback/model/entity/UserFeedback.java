package com.achobeta.domain.feedback.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName user_feedback
 */
@TableName(value ="user_feedback")
@Data
public class UserFeedback extends BaseIncrIDEntity implements Serializable {


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 招新批次
     */
    private Long batchId;

    /**
     * 处理结果的消息id
     */
    private Long messageId;

    /**
     * 反馈标题
     */
    private String title;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 附件链接
     */
    private String attchment;

    /**
     * 是否处理标记
     */
    @TableField("is_handle")
    private Boolean isHandle;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}