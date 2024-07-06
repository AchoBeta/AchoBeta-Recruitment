package com.achobeta.domain.qpaper.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName question_paper_library
 */
@TableName(value ="question_paper_library")
@Data
public class QuestionPaperLibrary extends BaseIncrIDEntity implements Serializable {

    private String libType;

    private static final long serialVersionUID = 1L;
}