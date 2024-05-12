package com.achobeta.domain.recruitment.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName recruitment
 */
@TableName(value ="recruitment")
@Data
public class Recruitment extends BaseIncrIDEntity implements Serializable {

    private Integer batch;

    private Boolean isRun;

    private Date deadline;

    private static final long serialVersionUID = 1L;
}