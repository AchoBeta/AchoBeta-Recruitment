package com.achobeta.domain.feedback.model.dto;

import com.achobeta.common.base.BasePageQueryEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/5
 */
@Getter
public class QueryUserOfFeedbackDTO extends BasePageQueryEntity implements Serializable {
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
