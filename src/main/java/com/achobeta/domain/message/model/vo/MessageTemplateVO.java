package com.achobeta.domain.message.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 查询消息模板返回对象
 * @date 2024/7/10
 */
@Data
public class MessageTemplateVO implements Serializable {

    /**
     * 模板消息id
     */
    private Long id;
    /**
     * 模板消息标题
     */
    private String templateTitle;

    /**
     * 模板消息内容
     */
    private String templateContent;
}
