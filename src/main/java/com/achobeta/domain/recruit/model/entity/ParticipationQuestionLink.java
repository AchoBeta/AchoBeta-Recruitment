package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName participation_question_link
 */
@TableName(value ="participation_question_link")
@Data
public class ParticipationQuestionLink extends BaseEntity implements Serializable {

    private Long participationId;

    private Long questionId;

    private String answer;

    private static final long serialVersionUID = 1L;
}