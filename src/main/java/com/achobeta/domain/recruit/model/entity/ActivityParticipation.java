package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName activity_participation
 */
@TableName(value ="activity_participation")
@Data
public class ActivityParticipation extends BaseIncrIDEntity implements Serializable {

    private Long stuId;

    private Long actId;

    private static final long serialVersionUID = 1L;
}