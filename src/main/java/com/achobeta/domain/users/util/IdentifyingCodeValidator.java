package com.achobeta.domain.users.util;

import java.util.Random;

public class IdentifyingCodeValidator {

    public static final String IDENTIFYING_CODE_PURPOSE = "验证用户身份";

    public static final int IDENTIFYING_CODE_SIZE = 6; // 验证码长度

    public static final int IDENTIFYING_CODE_RADIX = 10; // 验证码每位的取值范围，也可以说是进制

    public static final String IDENTIFYING_CODE = "IDENTIFYING_CODE";

    public static final String IDENTIFYING_DEADLINE = "IDENTIFYING_DEADLINE";

    public static final String REDIS_EMAIL_IDENTIFYING_CODE = "REDIS_EMAIL_IDENTIFYING_CODE_";

    public static String getIdentifyingCode() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < IDENTIFYING_CODE_SIZE; i++) {
            int number = random.nextInt(IDENTIFYING_CODE_RADIX);
            builder.append(Integer.toString(number, IDENTIFYING_CODE_RADIX));
        }
        return builder.toString();
    }

}
