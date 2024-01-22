package com.achobeta.domain.shortlink.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

@Slf4j
public class HttpUrlValidator {

    private static final Pattern HTTP_PATTERN = Pattern.compile("^(http|https)://.*$");

    private static final Integer HTTP_CODE = GlobalServiceStatusCode.SYSTEM_SUCCESS.getCode();

    /**
     * 是否格式正确
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        return HTTP_PATTERN.matcher(url).matches();
    }

    /**
     * 是否是有效地址
     *
     * @param urlString 链接
     * @return 是否有效
     */
    public static boolean isUrlAccessible(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            return HTTP_CODE.equals(responseCode); // 如果状态码为 200，则返回 true，表示可以访问
        } catch (IOException e) {
            log.warn("{} 无法访问", urlString);
            return false; // 发生异常时，返回 false，表示不可访问
        }
    }


}
