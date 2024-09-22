package com.achobeta.domain.resource.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.common.enums.ResourceAccessLevel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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

    private String fileName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}