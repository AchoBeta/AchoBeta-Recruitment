package com.achobeta.util;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-22
 * Time: 23:57
 */
@Slf4j
public class ImageUtil {

    @Nullable
    public static Image getImage(InputStream inputStream) {
        try {
            return Objects.nonNull(inputStream) ? ImageIO.read(inputStream) : null;
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Nullable
    public static Image getImage(String url) {
        try (InputStream inputStream = MediaUtil.getInputStream(url)) {
            return getImage(inputStream);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Nullable
    public static Image getImage(byte[] bytes) {
        try (InputStream inputStream = MediaUtil.getInputStream(bytes)) {
            return getImage(inputStream);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static boolean isImage(Image image) {
        return Optional.ofNullable(image)
                .filter(img -> img.getWidth(null) > 0)
                .filter(img -> img.getHeight(null) > 0)
                .map(img -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    public static boolean isImage(String url) {
        return Optional.ofNullable(url)
                .filter(StringUtils::hasText)
                .map(ImageUtil::getImage)
                .map(ImageUtil::isImage)
                .orElse(Boolean.FALSE);
    }

    public static boolean isImage(byte[] bytes) {
        return Optional.ofNullable(bytes)
                .map(ImageUtil::getImage)
                .map(ImageUtil::isImage)
                .orElse(Boolean.FALSE);
    }

    public static boolean isImage(InputStream inputStream) {
        return Optional.ofNullable(inputStream)
                .map(ImageUtil::getImage)
                .map(ImageUtil::isImage)
                .orElse(Boolean.FALSE);
    }

}
