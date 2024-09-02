package com.achobeta.domain.evaluate.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interview_question_score
 */
@TableName(value ="interview_question_score")
@Data
public class InterviewQuestionScore extends BaseIncrIDEntity implements Serializable {

    private Long interviewId;

    private Long questionId;

    private Integer score;

    private static final long serialVersionUID = 1L;
}