package com.achobeta.domain.paper.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName question_paper
 */
@TableName(value ="question_paper")
@Data
public class QuestionPaper extends BaseIncrIDEntity implements Serializable {

    private Long libId;

    private String title;

    private static final long serialVersionUID = 1L;
}