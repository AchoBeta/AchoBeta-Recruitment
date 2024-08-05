package com.achobeta.domain.message.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 消息模板添加dto
 * @date 2024/7/10
 */
@Data
public class AddMessageTemplateDTO implements Serializable {
    /**
     * 模板消息标题
     */
    @NotBlank(message = "标题不能为空")
    private String templateTitle;

    /**
     * 模板消息内容
     */
    @NotBlank(message = "内容不能为空")
    private String templateContent;
}
