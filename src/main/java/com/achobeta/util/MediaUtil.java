package com.achobeta.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private final static Tika TIKA = new Tika();

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

    @Nullable
    public static InputStream getInputStream(byte[] bytes) {
        return Objects.nonNull(bytes) ? new ByteArrayInputStream(bytes) : null;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        return Objects.nonNull(inputStream) ? inputStream.readAllBytes() : null;
    }

    public static byte[] getBytes(String url) {
        try (InputStream inputStream = getInputStream(url)) {
            return getBytes(inputStream);
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static String getContentType(InputStream inputStream, String suffix) throws IOException {
        return TIKA.detect(inputStream, suffix);
    }

    public static String getContentType(byte[] data, String suffix) {
        try(InputStream inputStream = MediaUtil.getInputStream(data)) {
            return getContentType(inputStream, suffix);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

}
