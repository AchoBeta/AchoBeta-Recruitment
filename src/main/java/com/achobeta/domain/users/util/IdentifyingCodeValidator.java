package com.achobeta.domain.users.util;

import cn.hutool.core.util.RandomUtil;

import java.util.Map;

public class IdentifyingCodeValidator {

    public static final String IDENTIFYING_CODE_PURPOSE = "验证用户身份";

    public static final int IDENTIFYING_CODE_SIZE = 6; // 验证码长度

    public static final String IDENTIFYING_CODE = "IdentifyingCode";

    public static final String IDENTIFYING_DEADLINE = "IdentifyingDeadline";

    public static final String IDENTIFYING_OPPORTUNITIES = "IdentifyingOpportunities";

    public static final String REDIS_EMAIL_IDENTIFYING_CODE = "REDIS_EMAIL_IDENTIFYING_CODE_";

    public static String getIdentifyingCode() {
        return RandomUtil.randomNumbers(IDENTIFYING_CODE_SIZE);
    }

    public static boolean isAllowedToSend(Map<String, Object> data, long intervalLimit, long timeout) {
        // 当前时间，例：1705574_448_000
        long nowTime = System.currentTimeMillis();
        // 截止时间，例：170557_474_8000
        long deadline = (long) data.get(IDENTIFYING_DEADLINE);
        // 下个可以再次发送的点，例：1705574_748_000 - 5 * 60 * 1_000 + 1 * 60 * 1_000 = 1705574_508_000
        long nextAllowedTime = deadline - timeout + intervalLimit;
        // 当前时间必须在这个点之前，例：1705574_904_000 > 1705574_508_000
        return nowTime > nextAllowedTime;
    }

}
