package com.achobeta.domain.student.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuSimpleResumeVO implements Serializable {


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

    private String image;

    private String awards;

    private String remark;
}
