package com.achobeta.domain.evaluate.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interview_summary
 */
@TableName(value ="interview_summary")
@Data
public class InterviewSummary extends BaseIncrIDEntity implements Serializable {

    private Long interviewId;

    private Integer basis;

    private Integer coding;

    private Integer thinking;

    private Integer express;

    private String evaluate;

    private String suggest;

    private String playback;

    private static final long serialVersionUID = 1L;
}