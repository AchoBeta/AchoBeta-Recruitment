package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName participation_question_link
 */
@TableName(value ="participation_question_link")
@Data
public class ParticipationQuestionLink extends BaseIncrIDEntity implements Serializable {

    private Long participationId;

    private Long questionId;

    private String answer;

    private static final long serialVersionUID = 1L;
}