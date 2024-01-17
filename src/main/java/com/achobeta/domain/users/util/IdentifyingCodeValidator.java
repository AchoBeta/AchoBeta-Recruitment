package com.achobeta.domain.users.util;

import cn.hutool.core.util.RandomUtil;

import java.util.Map;

public class IdentifyingCodeValidator {

    public static final String IDENTIFYING_CODE_PURPOSE = "验证用户身份";

    public static final int IDENTIFYING_CODE_SIZE = 6; // 验证码长度

    public static final String IDENTIFYING_CODE = "IDENTIFYING_CODE";

    public static final String IDENTIFYING_DEADLINE = "IDENTIFYING_DEADLINE";

    public static final String REDIS_EMAIL_IDENTIFYING_CODE = "REDIS_EMAIL_IDENTIFYING_CODE_";

    public static String getIdentifyingCode() {
        return RandomUtil.randomNumbers(IDENTIFYING_CODE_SIZE);
    }

    public static boolean isAllowedToSend(Map<String, Object> data, long intervalLimit, long timeout) {
        long nowTime = System.currentTimeMillis(); // 当前时间
        long deadline = (long)data.get(IDENTIFYING_DEADLINE); // 截止时间
        long nextAllowedTime = deadline - timeout + intervalLimit; // 下个可以再次发送的点
        return nowTime > nextAllowedTime;
    }

}
