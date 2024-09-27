package com.achobeta.domain.login.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:27
 */
@Getter
@Setter
@ToString
public class LoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 登录方式
     */
    @NotBlank(message = "登录方式不能为空")
    @JsonProperty("login_type")
    private String loginType;

    /**
     * 邮箱登录
     */
    @JsonProperty("email_params")
    @Valid
    private EmailLoginDTO emailParams;

    /**
     * 密码登录
     */
    @JsonProperty("password_params")
    @Valid
    private PasswordLoginDTO passwordParams;

    /**
     * 短信登录
     */
    @JsonProperty("sms_params")
    @Valid
    private SmsLoginDTO smsParams;

}
