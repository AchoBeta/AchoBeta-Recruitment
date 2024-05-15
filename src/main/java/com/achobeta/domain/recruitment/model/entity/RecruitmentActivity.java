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
 * @TableName recruitment_activity
 */
@TableName(value ="recruitment_activity")
@Data
public class RecruitmentActivity extends BaseIncrIDEntity implements Serializable {

    private Long paperId;

    private Integer batch;

    private Date deadline;

    private Boolean isRun;

    private static final long serialVersionUID = 1L;
}