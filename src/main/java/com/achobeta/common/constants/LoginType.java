package com.achobeta.common.constants;

import lombok.Getter;

@Getter
public enum LoginType {

    LOGINBYEMAIL("邮箱登录",1),
    LOGINBYPASSWORD("密码登录",2);

    private final Integer LoginCode;
    private final String LoginTypeName;

    LoginType(String loginTypeName, Integer loginCode) {
        LoginTypeName = loginTypeName;
        LoginCode = loginCode;
    }


}
