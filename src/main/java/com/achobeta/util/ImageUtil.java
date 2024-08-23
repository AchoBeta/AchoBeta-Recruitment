package com.achobeta.util;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-22
 * Time: 23:57
 */
public class ImageUtil {

    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return connection.getResponseCode() == HttpStatus.OK.value() ? connection.getInputStream() : null;
    }

    public static InputStream getInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public static Image getImage(String url) {
        try (InputStream inputStream = getInputStream(url)) {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            return null;
        }
    }

    public static Image getImage(byte[] bytes) {
        try (InputStream inputStream = getInputStream(bytes)) {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            return null;
        }
    }

    public static Image getImage(InputStream inputStream) {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    public static Boolean isImage(Image image) {
        return Optional.ofNullable(image)
                .filter(img -> img.getWidth(null) > 0)
                .filter(img -> img.getHeight(null) > 0)
                .map(img -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    public static Boolean isImage(String url) {
        return Optional.ofNullable(url)
                .filter(StringUtils::hasText)
                .map(ImageUtil::getImage)
                .map(ImageUtil::isImage)
                .orElse(Boolean.FALSE);
    }

    public static Boolean isImage(byte[] bytes) {
        return Optional.ofNullable(bytes)
                .map(ImageUtil::getImage)
                .map(ImageUtil::isImage)
                .orElse(Boolean.FALSE);
    }

    public static Boolean isImage(InputStream inputStream) {
        return Optional.ofNullable(inputStream)
                .map(ImageUtil::getImage)
                .map(ImageUtil::isImage)
                .orElse(Boolean.FALSE);
    }

}