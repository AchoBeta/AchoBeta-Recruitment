package com.achobeta.util;

import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 13:19
 */
public class MediaUtil {

    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return connection.getResponseCode() == HttpStatus.OK.value() ? connection.getInputStream() : null;
    }

    public static InputStream getInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

}
