package com.achobeta.domain.evaluate.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interview_comment
 */
@TableName(value ="interview_comment")
@Data
public class InterviewComment extends BaseIncrIDEntity implements Serializable {

    private Long interviewId;

    private Long managerId;

    private String content;

    private static final long serialVersionUID = 1L;
}