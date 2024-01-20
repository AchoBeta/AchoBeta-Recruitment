package com.achobeta.common.constants;

public enum LoginType {

    LOGINBYEMAIL("邮箱登录",1),
    LOGINBYPASSWORD("密码登录",2);

    private final Integer LoginCode;
    private final String LoginTypeName;

    LoginType(String loginTypeName, Integer loginCode) {
        LoginTypeName = loginTypeName;
        LoginCode = loginCode;
    }

    public String getLoginTypeName() {
        return LoginTypeName;
    }

    public Integer getLoginCode() {
        return LoginCode;
    }
}
