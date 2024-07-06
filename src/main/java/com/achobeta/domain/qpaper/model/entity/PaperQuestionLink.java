package com.achobeta.domain.qpaper.model.entity;

import com.achobeta.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName paper_question_link
 */
@TableName(value ="paper_question_link")
@Data
public class PaperQuestionLink extends BaseEntity implements Serializable {

    private Long paperId;

    private Long questionId;

    private static final long serialVersionUID = 1L;
}