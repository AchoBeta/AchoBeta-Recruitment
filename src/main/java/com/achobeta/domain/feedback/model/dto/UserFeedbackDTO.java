package com.achobeta.domain.feedback.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/10
 */
@Data
public class UserFeedbackDTO implements Serializable {

    /**
     * 招新批次
     */
    @NotNull(message = "招新批次 id 不能为空")
    private Long batchId;
    /**
     * 反馈标题
     */
    @NotBlank(message = "标题不能为空")
    private String tittle;

    /**
     * 反馈内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 附件链接
     */
    private Long attachment;


}
