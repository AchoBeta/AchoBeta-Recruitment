package com.achobeta.domain.login.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:36
 */
@Getter
@Setter
@ToString
public class SmsLoginDTO extends LoginDTO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;
}
