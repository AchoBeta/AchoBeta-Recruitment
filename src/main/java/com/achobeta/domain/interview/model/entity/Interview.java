package com.achobeta.domain.interview.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interview
 */
@TableName(value ="interview")
@Data
public class Interview implements Serializable {
    private Long id;

    private Long scheduleId;

    private Long managerId;

    private Long summaryId;

    private String title;

    private Integer state;

    private String interviewLink;

    private Integer version;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}