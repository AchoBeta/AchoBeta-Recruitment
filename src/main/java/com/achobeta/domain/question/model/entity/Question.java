package com.achobeta.domain.question.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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