package com.achobeta.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.achobeta.common.enums.HttpRequestEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(?i)(http|https):(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}:.]*[%\\p{Alnum}]*]|[^\\[/?#:]*)(:(\\{[^}]+\\}?|[^/?#]*))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");

    public static boolean isHttpUrl(String url) {
        return StringUtils.hasText(url) && HTTP_URL_PATTERN.matcher(url).matches();
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

    public static String hiddenQueryString(String url) {
        return StringUtils.hasText(url) && url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
    }

    public static String buildUrl(String baseUrl, Map<String, List<String>> queryParams, Object... uriVariableValues) {
        queryParams = Optional.ofNullable(queryParams).orElseGet(Map::of);
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParams(new LinkedMultiValueMap<>(queryParams))
                .buildAndExpand(uriVariableValues)
                .encode()
                .toUriString();
    }

    public static <P> String buildUrl(String baseUrl, Map<String, List<String>> queryParams, Map<String, P> pathParams) {
        queryParams = Optional.ofNullable(queryParams).orElseGet(Map::of);
        pathParams = Optional.ofNullable(pathParams).orElseGet(Map::of);
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParams(new LinkedMultiValueMap<>(queryParams))
                .buildAndExpand(pathParams)
                .encode()
                .toUriString();
    }

    public static <R, T> R jsonRequest(String url, String method, T requestBody, Class<R> responseClazz, Map<String, String> headers) {
        // 准备参数
        Method requestMethod = Method.valueOf(method.toUpperCase());
        headers = Optional.ofNullable(headers).orElseGet(Map::of);
        String reqJson = GsonUtil.toJson(requestBody);
        // 发出请求
        String respJson = HttpUtil.createRequest(requestMethod, url)
                .headerMap(headers, Boolean.TRUE)
                .headerMap(JSON_CONTENT_TYPE_HEADER, Boolean.TRUE)
                .body(reqJson)
                .execute()
                .body();
        // 转换并返回
        return GsonUtil.fromJson(respJson, responseClazz);
    }

    public static <R, T> R jsonRequest(HttpRequestEnum requestEnum, T requestBody, Class<R> responseClazz, Map<String, String> headers) {
        return jsonRequest(requestEnum.getUrl(), requestEnum.getMethod(), requestBody, responseClazz, headers);
    }

    public static <R, T, P> R jsonRequest(HttpRequestEnum requestEnum, T requestBody, Class<R> responseClazz,
                                       Map<String, String> headers, Map<String, List<String>> queryParams, Map<String, P> pathParams) {
        String url = buildUrl(requestEnum.getUrl(), queryParams, pathParams);
        return jsonRequest(url, requestEnum.getMethod(), requestBody, responseClazz, headers);
    }

    public static <R, T> R jsonRequest(HttpRequestEnum requestEnum, T requestBody, Class<R> responseClazz,
                                       Map<String, String> headers, Map<String, List<String>> queryParams, Object... uriVariableValues) {
        String url = buildUrl(requestEnum.getUrl(), queryParams, uriVariableValues);
        return jsonRequest(url, requestEnum.getMethod(), requestBody, responseClazz, headers);
    }

}
