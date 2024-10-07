package com.achobeta.domain.feedback.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/5
 */
@Getter
@Setter
public class QueryUserOfFeedbackDTO implements Serializable {

    private Integer current;

    private Integer pageSize;

    /**
     * 招新批次
     */
    @NotNull(message = "招新批次不能为空")
    private Integer batchId;
    /**
     * 是否处理标记
     */
    private Boolean isHandle;
}
