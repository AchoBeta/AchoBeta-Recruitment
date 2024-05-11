package com.achobeta.domain.interview.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interview_schedule
 */
@TableName(value ="interview_schedule")
@Data
public class InterviewSchedule implements Serializable {
    private Long id;

    private Long stuId;

    private Date date;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Integer version;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}