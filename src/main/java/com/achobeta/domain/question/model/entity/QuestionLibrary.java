package com.achobeta.domain.question.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName question_library
 */
@TableName(value ="question_library")
@Data
public class QuestionLibrary extends BaseIncrIDEntity implements Serializable {

    private String libType;

    private static final long serialVersionUID = 1L;
}