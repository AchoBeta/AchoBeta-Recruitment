package com.achobeta.domain.feedback.model.vo;

import com.achobeta.domain.feedback.model.entity.StuBaseInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author cattleYuan
 * @date 2024/7/10
 */
@Data
public class UserFeedbackVO implements Serializable {

    /**
     * 反馈id
     */
    private Long id;
    /**
     * 处理结果的消息id
     */
    private Long messageId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 反馈学生基本信息
     */
    private StuBaseInfo stuBaseInfo;
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
    private long attachment;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 是否处理标记
     */
    @TableField("is_handle")
    private Boolean isHandle;
}
