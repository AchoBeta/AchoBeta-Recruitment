package com.achobeta.domain.interview.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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