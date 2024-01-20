package com.achobeta.domain.users.model.po;

import java.io.Serializable;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 学生用户简历表
 * @TableName student
 */
@Data
@TableName("student")
public class StudentEntity extends BaseIncrIDEntity implements Serializable {

    // 问卷id
    private Integer questionnaireId;

    // 学号
    private String studentId;

    // 姓名
    @TableField("name")
    private String name;

    // 性别
    private Integer gender;

    // 专业
    private String major;

    // 班级
    @TableField("class")
    private String classId;

    // 邮箱
    @TableField("email")
    private String email;

    // 手机号码
    private String phoneNumber;

    // 加入 AchoBeta 的理由
    private String reason;

    // 个人介绍（自我认知）
    private String introduce;

    // 个人经历 （项目经历、 职业规划等）
    private String experience;

    // 获奖经历
    private String awards;

    // 照片
    private String image;

    // 备注
    private String remark;

    // 简历状态
    private Integer status;

    // 提交次数
    private Integer submitCount;

    // ab版本
    private Integer batch;

    private static final long serialVersionUID = 1L;
}