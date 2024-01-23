package com.achobeta.domain.login.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:31
 */
@Getter
@Setter
@ToString
public class EmailLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱地址
     */
    @Email
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 邮箱验证码
     */
    @NotBlank(message = "邮箱验证码不能为空")
    private String emailCode;
}
