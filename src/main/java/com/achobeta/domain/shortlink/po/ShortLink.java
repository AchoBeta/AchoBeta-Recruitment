package com.achobeta.domain.shortlink.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName short_link
 */
@TableName(value ="short_link")
@Data
public class ShortLink implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String originUrl;

    private String shortCode;

    private Boolean isUsed;

    private Boolean isDeleted;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}