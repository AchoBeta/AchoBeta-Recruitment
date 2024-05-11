package com.achobeta.domain.recruitment.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName questionnaire
 */
@TableName(value ="questionnaire")
@Data
public class Questionnaire extends BaseIncrIDEntity implements Serializable {

    private Long stuId;

    private Long recId;

    private static final long serialVersionUID = 1L;
}