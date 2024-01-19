package com.achobeta.domain.shortlink.po;

import com.achobeta.domain.users.model.dao.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName short_link
 */
@TableName(value = "short_link")
@Data
public class ShortLink extends BaseIncrIDEntity implements Serializable {

    @TableField(value = "origin_url")
    private String originUrl;

    @TableField(value = "short_code")
    private String shortCode;

    @TableField(value = "is_used")
    private Boolean isUsed;

    private static final long serialVersionUID = 1L;
}