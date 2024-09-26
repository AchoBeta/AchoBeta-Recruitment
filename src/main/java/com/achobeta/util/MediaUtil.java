package com.achobeta.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 13:19
 */
@Slf4j
public class MediaUtil {

    private final static Tika TIKA = new Tika();

    public static boolean isAccessible(HttpURLConnection connection) throws IOException {
        return Objects.nonNull(connection) && connection.getResponseCode() / 100 == 2;
    }

    public static boolean isAccessible(String url) throws IOException {
        return isAccessible(HttpRequestUtil.openConnection(url));
    }

    @Nullable
    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = HttpRequestUtil.openConnection(url);
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

    public static String getContentType(InputStream inputStream) throws IOException {
        return TIKA.detect(inputStream);
    }

    public static String getContentType(MultipartFile file) {
        try(InputStream inputStream = file.getInputStream()) {
            return getContentType(inputStream);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static String getContentType(byte[] data) {
        try(InputStream inputStream = MediaUtil.getInputStream(data)) {
            return getContentType(inputStream);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

}
