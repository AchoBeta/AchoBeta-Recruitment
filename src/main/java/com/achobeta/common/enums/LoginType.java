package com.achobeta.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.achobeta.common.enums.GlobalServiceStatusCode.USER_CAPTCHA_CODE_ERROR;
import static com.achobeta.common.enums.GlobalServiceStatusCode.USER_CREDENTIALS_ERROR;

@Getter
@AllArgsConstructor
public enum LoginType {

    EMAIL("邮箱登录", USER_CAPTCHA_CODE_ERROR),
    PASSWORD("密码登录", USER_CREDENTIALS_ERROR),
    SMS("手机登录", USER_CAPTCHA_CODE_ERROR),
    ;

    private final String message;
    private final GlobalServiceStatusCode errorCode;

}
