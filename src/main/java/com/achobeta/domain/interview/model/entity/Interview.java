package com.achobeta.domain.interview.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interview
 */
@TableName(value ="interview")
@Data
public class Interview extends BaseIncrIDEntity implements Serializable {

    private Long scheduleId;

    private Long paperId;

    private String title;

    private String description;

    private InterviewStatus status;

    private String address;

    private static final long serialVersionUID = 1L;
}