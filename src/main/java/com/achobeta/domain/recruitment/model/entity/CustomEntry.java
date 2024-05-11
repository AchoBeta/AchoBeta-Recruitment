package com.achobeta.domain.recruitment.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName custom_entry
 */
@TableName(value ="custom_entry")
@Data
public class CustomEntry extends BaseIncrIDEntity implements Serializable {

    private Long recId;

    private String title;

    private static final long serialVersionUID = 1L;
}