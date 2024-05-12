package com.achobeta.domain.recruitment.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

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