package com.achobeta.domain.student.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.common.enums.ResumeStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName stu_resume
 */
@TableName(value ="stu_resume")
@Data
public class StuResume extends BaseIncrIDEntity implements Serializable {

    private Long userId;

    private Long batchId;

    private String studentId;

    private String name;

    private Integer gender;

    private Integer grade;

    private String major;

    @TableField("class")
    private String className;

    private String email;

    private String phoneNumber;

    private String reason;

    private String introduce;

    private String experience;

    private String awards;

    private Long image;

    private String remark;

    private ResumeStatus status;

    private Integer submitCount;

    private static final long serialVersionUID = 1L;
}