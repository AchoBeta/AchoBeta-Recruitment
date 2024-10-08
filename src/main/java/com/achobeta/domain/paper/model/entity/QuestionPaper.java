package com.achobeta.domain.paper.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName question_paper
 */
@TableName(value ="question_paper")
@Data
public class QuestionPaper extends BaseIncrIDEntity implements Serializable {

    private String title;

    private String description;

    private static final long serialVersionUID = 1L;
}