package com.achobeta.domain.email.util;

import cn.hutool.core.util.RandomUtil;

import java.util.Map;

public class IdentifyingCodeValidator {

    public static final String IDENTIFYING_CODE_PURPOSE = "验证用户身份";

    public static final int IDENTIFYING_CODE_SIZE = 6; // 验证码长度

    public static final String IDENTIFYING_CODE = "IdentifyingCode"; // 验证码

    public static final String IDENTIFYING_OPPORTUNITIES = "IdentifyingOpportunities"; // 剩余验证验证机会

//    public static final String REDIS_EMAIL_IDENTIFYING_CODE = "REDIS_EMAIL_IDENTIFYING_CODE_";
    public static final String REDIS_EMAIL_IDENTIFYING_CODE = "redis_email_identifying_code:";

    public static String getIdentifyingCode() {
        return RandomUtil.randomNumbers(IDENTIFYING_CODE_SIZE);
    }

}
