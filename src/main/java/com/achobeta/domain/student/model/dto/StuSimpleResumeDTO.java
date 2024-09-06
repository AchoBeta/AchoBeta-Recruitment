package com.achobeta.domain.student.model.dto;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.common.annotation.IsImage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "学号不能为空")
    private String studentId;

    @NotBlank(message = "名字不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    @IntRange(min = 0, max = 1, message = "性别值非法")
    private Integer gender;

    @NotNull(message = "年级不能为空")
    @IntRange(min = 1000, max = 9999, message = "年级应为四位数")
    private Integer grade;

    @NotBlank(message = "专业不能为空")
    private String major;

    @NotBlank(message = "班名不能为空")
    private String className;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱非法")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号非法")
    private String phoneNumber;

    @NotBlank(message = "加入ab理由不能为空")
    private String reason;

    @NotBlank(message = "个人介绍不能为空")
    private String introduce;

    @NotBlank(message = "个人经历不能为空")
    private String experience;

    @NotNull(message = "证件照不能为空")
    @IsImage(message = "证件照非法")
    private String image;

    private String awards;

    private String remark;
}
