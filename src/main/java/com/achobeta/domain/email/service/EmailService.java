package com.achobeta.domain.email.service;

public interface EmailService {

    /**
     * 向用户邮箱发送验证码
     *
     * @param email 用户的邮箱
     * @param code  验证码
     */
    void sendIdentifyingCode(String email, String code);

}
