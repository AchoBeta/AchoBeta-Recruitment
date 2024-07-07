package com.achobeta.domain.paper.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName paper_question_link
 */
@TableName(value ="paper_question_link")
@Data
public class PaperQuestionLink extends BaseIncrIDEntity implements Serializable {

    private Long paperId;

    private Long questionId;

    private static final long serialVersionUID = 1L;
}