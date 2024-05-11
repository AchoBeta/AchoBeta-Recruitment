package com.achobeta.domain.interview.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName interview_comment
 */
@TableName(value ="interview_comment")
@Data
public class InterviewComment implements Serializable {
    private Long id;

    private Long interviewId;

    private Long managerId;

    private String content;

    private Integer version;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}