package com.achobeta.domain.feedback.model.vo;

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
     * 反馈标题
     */
    private String title;

    /**
     * 反馈内容
     */
    private String content;

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
