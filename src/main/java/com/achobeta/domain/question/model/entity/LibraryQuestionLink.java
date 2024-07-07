package com.achobeta.domain.question.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName library_question_link
 */
@TableName(value ="library_question_link")
@Data
public class LibraryQuestionLink extends BaseIncrIDEntity implements Serializable {

    private Long libId;

    private Long questionId;

    private static final long serialVersionUID = 1L;
}