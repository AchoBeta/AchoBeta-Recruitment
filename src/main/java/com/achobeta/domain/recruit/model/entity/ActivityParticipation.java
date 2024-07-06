package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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