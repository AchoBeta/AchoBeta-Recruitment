package com.achobeta.domain.student.model.dto;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.common.annotation.MobilePhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuSimpleResumeDTO implements Serializable {

    @NotNull(message = "版本号不能为空")
    private Long batchId;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^20\\d{9,11}$", message = "学号非法")
    private String studentId;

    @NotBlank(message = "名字不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    @IntRange(min = 0, max = 1, message = "性别值非法")
    private Integer gender;

    @NotNull(message = "年级不能为空")
    @IntRange(min = 2000, max = 2100, message = "年级应为四位数")
    private Integer grade;

    @NotBlank(message = "专业不能为空")
    private String major;

    @NotBlank(message = "班名不能为空")
    private String className;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱非法")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @MobilePhone
    private String phoneNumber;

    @NotBlank(message = "加入ab理由不能为空")
    private String reason;

    @NotBlank(message = "个人介绍不能为空")
    private String introduce;

    @NotBlank(message = "个人经历不能为空")
    private String experience;

    @NotNull(message = "证件照不能为空")
    private Long image;

    private String awards;

    private String remark;
}
