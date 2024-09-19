package com.achobeta.domain.schedule.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interviewer
 */
@TableName(value ="interviewer")
@Data
public class Interviewer extends BaseIncrIDEntity implements Serializable {

    private Long managerId;

    private Long scheduleId;

    private static final long serialVersionUID = 1L;
}