package com.achobeta.domain.feedback.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

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
    private String tittle;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 附件链接
     */
    private String attachment;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;


    /**
     * 是否处理标记
     */
    @TableField("is_handle")
    private Boolean isHandle;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}