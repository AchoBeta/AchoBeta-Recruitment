package com.achobeta.domain.shortlink.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class ShortLinkUtils {

    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    private static final int LINK_LENGTH = 6;

    public static final String LINK = "REDIS_SHORT_LINK_";


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
        if(gap < 4) {
            return null;
        }
        StringBuilder subCode = new StringBuilder();
        int modulus = CHARSET.length();
        for (int i = 0; i < LINK_LENGTH; i++) {
            int index = Integer.parseInt(str.substring(i * gap, i * gap + 4), 16);//提取十六进制数
            subCode.append(CHARSET.charAt(index % modulus));//对应到Base64字典的某个Base64字符
        }
        return subCode.toString();
    }

    public static String getShortCodeByURL(String url) {
        String hash = md5(url + getSalt());
        return subCodeByString(hash);
    }

    public static String getBaseUrl(String host) {
        return String.format("http://%s/api/v1/shortlink/", host);
    }



}
