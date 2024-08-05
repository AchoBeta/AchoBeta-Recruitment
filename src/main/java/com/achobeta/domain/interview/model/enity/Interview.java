package com.achobeta.domain.interview.model.enity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interview
 */
@TableName(value ="interview")
@Data
public class Interview extends BaseIncrIDEntity implements Serializable {

    private Long scheduleId;

    private Long paperId;

    private String title;

    private String description;

    private InterviewStatusEnum status;

    private String address;

    private static final long serialVersionUID = 1L;
}