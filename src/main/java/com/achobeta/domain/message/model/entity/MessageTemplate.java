package com.achobeta.domain.message.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 模板消息表
 * @TableName message_template
 */
@TableName(value ="message_template")
@Data
public class MessageTemplate extends BaseIncrIDEntity implements Serializable {

    /**
     * 模板消息标题
     */
    private String templateTitle;

    /**
     * 模板消息内容
     */
    private String templateContent;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}