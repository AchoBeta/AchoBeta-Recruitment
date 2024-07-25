package com.achobeta.domain.interview.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interviewer
 */
@TableName(value ="interviewer")
@Data
public class Interviewer implements Serializable {
    private Long id;

    private Long managerId;

    private Long scheduleId;

    private Integer version;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}