package com.achobeta.domain.student.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 22:52
 */
@Data
public class StuResumeDTO {

    private Long batchId;

    private String studentId;

    private String name;

    private Integer gender;

    private Integer grade;

    private String major;

    private String className;

    private String email;

    private String phoneNumber;

    private String reason;

    private String introduce;

    private String experience;

    private String awards;

    private String image;

    private String remark;

}
