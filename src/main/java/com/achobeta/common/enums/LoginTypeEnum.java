package com.achobeta.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.achobeta.common.enums.GlobalServiceStatusCode.USER_CAPTCHA_CODE_ERROR;
import static com.achobeta.common.enums.GlobalServiceStatusCode.USER_CREDENTIALS_ERROR;

/**
 * 这里使用 GlobalServiceStatusCode 的原因是不同的登录方式错误类型不同，这样写避免 if-else
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    EMAIL("email", USER_CAPTCHA_CODE_ERROR),
    PASSWORD("password", USER_CREDENTIALS_ERROR),
    SMS("sms", USER_CAPTCHA_CODE_ERROR),
    ;

    private final String message;
    private final GlobalServiceStatusCode errorCode;

}
