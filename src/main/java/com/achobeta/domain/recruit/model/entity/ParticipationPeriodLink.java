package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName participation_period_link
 */
@TableName(value ="participation_period_link")
@Data
public class ParticipationPeriodLink extends BaseIncrIDEntity implements Serializable {

    private Long participationId;

    private Long periodId;

    private static final long serialVersionUID = 1L;
}