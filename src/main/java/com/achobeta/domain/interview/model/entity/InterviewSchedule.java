package com.achobeta.domain.interview.model.entity;

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

    private Long participationId;

    private Date startTime;

    private Date endTime;

    private Integer version;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}