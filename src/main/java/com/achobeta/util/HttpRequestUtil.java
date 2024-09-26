package com.achobeta.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.exception.GlobalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 7:41
 */
public class HttpRequestUtil {

    public final static Map<String, String> JSON_CONTENT_TYPE_HEADER = Map.of("CONTENT_TYPE", "application/json; charset=utf-8");

    private final static ObjectMapper OBJECT_MAPPER;

    static {
        // 忽略未定义的属性
        OBJECT_MAPPER = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
    }

    private static final Pattern HTTP_PATTERN = Pattern.compile("^(http|https)://.*$");

    public static boolean isHttpUrl(String url) {
        return StringUtils.hasText(url) && HTTP_PATTERN.matcher(url).matches();
    }

    @Nullable
    public static HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection connection = isHttpUrl(url) ? (HttpURLConnection) new URL(url).openConnection() : null;
        if(Objects.nonNull(connection) && connection.getResponseCode() / 100 == 3) {
            return openConnection(connection.getHeaderField("Location")); // Location 就是最深的那个地址了
        } else {
            return connection;
        }
    }

    public static  <R, T> R jsonRequest(HttpRequestEnum requestEnum, T requestBody, Class<R> responseClazz, Map<String, String> headers) {
        try {
            String json = HttpUtil.createRequest(Method.valueOf(requestEnum.getMethod()), requestEnum.getUrl())
                    .headerMap(headers, Boolean.TRUE)
                    .headerMap(JSON_CONTENT_TYPE_HEADER, Boolean.TRUE)
                    .body(OBJECT_MAPPER.writeValueAsString(requestBody))
                    .execute()
                    .body();
            return OBJECT_MAPPER.readValue(json, responseClazz);
        } catch (JsonProcessingException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
