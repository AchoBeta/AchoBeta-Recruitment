package com.achobeta.domain.shortlink.util;


import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class ShortLinkUtils {

    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

    private static final int LINK_LENGTH = 6;

    public static final String REDIS_SHORT_LINK = "redisShortLink:";//前缀

    public static final int FETCH_RADIX = 16;

    public static final int MODULES = CHARSET.length();

    public static final int FETCH_SIZE = 4;

    // 获取盐值
    public static String getSalt() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // md5加密
    public static String md5(String normal) {
        return DigestUtils.md5Hex(normal);
    }

    public static String subCodeByString(String str) {
        int strLength = str.length();
        int gap = strLength / LINK_LENGTH;//取值间隔
        if (gap < FETCH_SIZE) {
            // 代表无法取出6个十六进制数
            String message = String.format("哈希字符串%s，无法取出%d个%d进制数", str, LINK_LENGTH, FETCH_RADIX);
            throw new GlobalServiceException(message, GlobalServiceStatusCode.PARAM_NOT_VALID);
        }
        StringBuilder subCode = new StringBuilder();
        for (int i = 0; i < LINK_LENGTH; i++) {
            int index = Integer.parseInt(str.substring(i * gap, i * gap + FETCH_SIZE), FETCH_RADIX);//提取十六进制数
            subCode.append(CHARSET.charAt(index % MODULES));//对应到Base64字典的某个Base64字符
        }
        return subCode.toString();
    }

    public static String getShortCodeByURL(String url) {
        String hash = md5(url + getSalt());
        return subCodeByString(hash);
    }

}
