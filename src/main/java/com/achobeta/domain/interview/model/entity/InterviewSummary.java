package com.achobeta.domain.interview.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interview_summary
 */
@TableName(value ="interview_summary")
@Data
public class InterviewSummary implements Serializable {
    private Long id;

    private Integer basis;

    private Integer coding;

    private Integer thinking;

    private Integer express;

    private String evaluate;

    private String suggest;

    private String playbackLink;

    private Integer version;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}