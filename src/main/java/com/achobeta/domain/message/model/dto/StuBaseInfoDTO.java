package com.achobeta.domain.message.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/3
 */
@Getter
@Setter
public class StuBaseInfoDTO implements Serializable {
    /*发送用户id*/
    @NotNull(message = "用户 id 不能为空")
    private Long userId;
    /*用户姓名*/
    @NotBlank(message = "学生名称不能为空")
    private String stuName;
    /*用户邮箱*/
    @Email(message = "学生邮箱不能为空")
    private String email;
}
