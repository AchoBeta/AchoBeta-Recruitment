package com.achobeta.domain.interview.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interview_schedule
 */
@TableName(value ="interview_schedule")
@Data
public class InterviewSchedule extends BaseIncrIDEntity implements Serializable {

    private Long participationId;

    private Date startTime;

    private Date endTime;

    private static final long serialVersionUID = 1L;
}