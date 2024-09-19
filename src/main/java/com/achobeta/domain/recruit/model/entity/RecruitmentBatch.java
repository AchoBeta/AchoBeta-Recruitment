package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName recruitment_batch
 */
@TableName(value ="recruitment_batch")
@Data
public class RecruitmentBatch extends BaseIncrIDEntity implements Serializable {

    private Integer batch;

    private String title;

    private Date deadline;

    private Boolean isRun;

    private static final long serialVersionUID = 1L;
}