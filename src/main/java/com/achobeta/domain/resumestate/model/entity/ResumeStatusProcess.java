package com.achobeta.domain.resumestate.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 招新简历状态过程表
 * @TableName resume_status_process.sql
 */
@TableName(value ="resume_status_process")
@Data
public class ResumeStatusProcess extends BaseIncrIDEntity implements Serializable {

    /**
     * 简历 id
     */
    private Long resumeId;

    /**
     * 简历状态
     */
    private ResumeStatus resumeStatus;

    /**
     * 简历事件
     */
    private ResumeEvent resumeEvent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}