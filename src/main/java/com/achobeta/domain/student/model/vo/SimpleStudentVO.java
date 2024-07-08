package com.achobeta.domain.student.model.vo;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 23:18
 */
@Data
public class SimpleStudentVO {

    private Long resumeId;

    private Long userId;

    private Long studentId;

    private String name;

    private String username;

    private Integer gender;

    private Integer grade;

    private String major;

    private String className;

    private Integer status;
}
