package com.achobeta.domain.feedback.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/4
 */
@Getter
@Setter
public class UserPersonalFeedBackVO implements Serializable {

    /**
     * 反馈id
     */
    private Long id;
    /**
     * 处理结果的消息id
     */
    private Long messageId;

    /**
     * 招新批次
     */
    private Integer batchId;
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
