package com.achobeta.domain.users.service;

public interface EmailService {

    /**
     * 校验当前邮箱用户输入的验证码是否正确
     *
     * @param email 用户的邮箱
     * @param code  验证码
     */
    void sendIdentifyingCode(String email, String code);

    /**
     * 通过邮箱判断用户输入的验证码是否正确
     *
     * @param email 用户的邮箱
     * @param code  验证码
     */
    void checkIdentifyingCode(String email, String code);
}
