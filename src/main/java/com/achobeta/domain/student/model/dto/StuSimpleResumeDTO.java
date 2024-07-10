package com.achobeta.domain.student.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuSimpleResumeDTO implements Serializable {

    @NotNull(message = "版本号不能为空")
    private Long batchId;
    @NotNull(message = "学号不能为空")
    private Long studentId;
    @NotBlank(message = "名字不能为空")
    private String name;
    @NotNull(message = "性别不能为空")
    private Integer gender;
    @NotNull(message = "年级不能为空")
    private Integer grade;
    @NotBlank(message = "专业不能为空")
    private String major;
    @NotBlank(message = "班名不能为空")
    private String className;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;
    @NotBlank(message = "加入ab理由不能为空")
    private String reason;
    @NotBlank(message = "个人介绍不能为空")
    private String introduce;
    @NotBlank(message = "个人经历不能为空")
    private String experience;
    @NotBlank(message = "照片不能为空")
    private String image;

    private String awards;

    private String remark;
}
