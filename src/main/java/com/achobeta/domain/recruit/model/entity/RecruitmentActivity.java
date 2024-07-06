package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * @TableName recruitment_activity
 */
@TableName(value ="recruitment_activity", autoResultMap = true)
@Data
public class RecruitmentActivity extends BaseIncrIDEntity implements Serializable {

    private Long batchId;

    private Long paperId;

    private String title;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private StudentGroup target;

    private String description;

    private Date deadline;

    private Boolean isRun;

    private static final long serialVersionUID = 1L;
}