package com.achobeta.domain.feedback.model.dto;

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
public class UserFeedbackDTO implements Serializable {

    /**
     * 招新批次
     */
    private Long batchId;
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


}
