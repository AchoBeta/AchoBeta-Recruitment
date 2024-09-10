package com.achobeta.domain.question.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question extends BaseIncrIDEntity implements Serializable {

    private String title;

    private String standard;

    private static final long serialVersionUID = 1L;
}