package com.achobeta.util;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 13:19
 */
@Slf4j
public class MediaUtil {

    private static final Pattern HTTP_PATTERN = Pattern.compile("^(http|https)://.*$");

    public static boolean isHttpUrl(String url) {
        return StringUtils.hasText(url) && HTTP_PATTERN.matcher(url).matches();
    }

    @Nullable
    public static HttpURLConnection openConnection(String url) throws IOException {
        try {
            HttpURLConnection connection = isHttpUrl(url) ? (HttpURLConnection) new URL(url).openConnection() : null;
            if(Objects.nonNull(connection) && connection.getResponseCode() / 100 == 3) {
                return openConnection(connection.getHeaderField("Location")); // Location 就是最深的那个地址了
            } else {
                return connection;
            }
        } catch (ProtocolException | UnknownHostException e) {
            // 处理重定向次数太多的情况
            log.warn(e.getMessage());
            return null;
        }
    }

    public static boolean isAccessible(HttpURLConnection connection) throws IOException {
        return Objects.nonNull(connection) && connection.getResponseCode() / 100 == 2;
    }

    public static boolean isAccessible(String url) throws IOException {
        return isAccessible(openConnection(url));
    }

    @Nullable
    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = openConnection(url);
        return isAccessible(connection) ? connection.getInputStream() : null;
    }

    public static InputStream getInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }

    public static byte[] getBytes(String url) {
        try (InputStream inputStream = getInputStream(url)) {
            return Objects.nonNull(inputStream) ? getBytes(inputStream) : null;
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

}
