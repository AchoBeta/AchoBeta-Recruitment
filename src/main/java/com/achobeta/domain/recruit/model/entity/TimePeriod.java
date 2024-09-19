package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName time_period
 */
@TableName(value ="time_period")
@Data
public class TimePeriod extends BaseIncrIDEntity implements Serializable {

    private Long actId;

    private Date startTime;

    private Date endTime;

    private static final long serialVersionUID = 1L;
}