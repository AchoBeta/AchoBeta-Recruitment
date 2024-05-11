package com.achobeta.domain.recruitment.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @TableName time_period
 */
@TableName(value ="time_period")
@Data
public class TimePeriod extends BaseIncrIDEntity implements Serializable {

    private Long recId;

    private Date startTime;

    private Date endTime;

    private static final long serialVersionUID = 1L;
}