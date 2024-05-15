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
 * @TableName question_entry
 */
@TableName(value ="question_entry")
@Data
public class QuestionEntry extends BaseIncrIDEntity implements Serializable {

    private Long libId;

    private String title;

    private static final long serialVersionUID = 1L;
}