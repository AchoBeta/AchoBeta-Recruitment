package com.achobeta.domain.resource.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.common.enums.ResourceAccessLevel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 资源表
 * @TableName digital_resource
 */
@TableName(value ="digital_resource")
@Data
public class DigitalResource extends BaseIncrIDEntity implements Serializable {

    private Long code;

    private Long userId;

    private ResourceAccessLevel accessLevel;

    private String originalName;

    private String fileName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}