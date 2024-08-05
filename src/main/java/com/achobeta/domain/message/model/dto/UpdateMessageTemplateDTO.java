package com.achobeta.domain.message.model.dto;

import lombok.Data;

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
    Long id;

    /**
     * 模板消息标题
     */
    private String templateTitle;

    /**
     * 模板消息内容
     */
    private String templateContent;
}
