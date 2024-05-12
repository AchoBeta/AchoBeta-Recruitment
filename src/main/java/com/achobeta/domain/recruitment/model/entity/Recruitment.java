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