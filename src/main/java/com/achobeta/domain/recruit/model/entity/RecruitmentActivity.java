package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.domain.recruit.model.condition.StudentGroup;
import com.achobeta.handler.MyBatisJacksonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName recruitment_activity
 */
@TableName(value ="recruitment_activity", autoResultMap = true)
@Data
public class RecruitmentActivity extends BaseIncrIDEntity implements Serializable {

    private Long batchId;

    private Long paperId;

    private String title;

    @TableField(typeHandler = MyBatisJacksonTypeHandler.class) // 这里和 xml 两边都得设置，一个针对 java，一个针对 mysql
    private StudentGroup target;

    private String description;

    private Date deadline;

    private Boolean isRun;

    private static final long serialVersionUID = 1L;
}