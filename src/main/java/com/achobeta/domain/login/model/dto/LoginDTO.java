package com.achobeta.domain.login.model.dto;

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
    private String loginType;

    /**
     * 用户唯一标识 (uid + uuid)
     */
    private String openid;

    /**
     * 邮箱登录
     */
    private EmailLoginDTO emailParams;

    /**
     * 密码登录
     */
    private PasswordLoginDTO passwordParams;

    /**
     * 短信登录
     */
    private SmsLoginDTO smsParams;

}
