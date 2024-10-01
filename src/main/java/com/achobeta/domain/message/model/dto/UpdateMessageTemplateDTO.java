package com.achobeta.domain.message.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 更新模板dto
 * @date 2024/7/10
 */
@Data
public class UpdateMessageTemplateDTO implements Serializable {
    /**
     * 模板消息id
     */
    @NotNull(message = "模板消息不能为空")
    Long id;

    /**
     * 模板消息标题
     */
    @NotBlank(message = "模板标题不能为空")
    private String templateTitle;

    /**
     * 模板消息内容
     */
    @NotBlank(message = "模板内容不能为空")
    private String templateContent;
}
