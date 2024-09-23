package com.achobeta.domain.resource.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.config.DateTimeConfig;
import com.achobeta.exception.GlobalServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 12:31
 */
public class ResourceUtil {

    private final static String IMAGE_CONTENT_TYPE_PREFIX = "image";

    private final static String VIDEO_CONTENT_TYPE_PREFIX = "video";

    public static void checkOriginalName(String originalName) {
        // 判断是否有非空字符以及是否有后缀
        if (!StringUtils.hasText(originalName) || !originalName.contains(".")) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static void checkType(String contentType, String prefix) {
        if(!contentType.startsWith(prefix)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_TYPE_NOT_MATCH);
        }
    }

    public static void checkImage(String contentType) {
        checkType(contentType, IMAGE_CONTENT_TYPE_PREFIX);
    }

    public static void checkVideo(String contentType) {
        checkType(contentType, VIDEO_CONTENT_TYPE_PREFIX);
    }

    public static String getOriginalName(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        checkOriginalName(originalName);
        return originalName;
    }

    public static String getFileNameSuffix(String originalName) {
        checkOriginalName(originalName);
        return originalName.substring(originalName.lastIndexOf("."));
    }

    public static String getUniqueFileName(String suffix) {
        return String.format(
                "%s/%s/%s%s",
                DateTimeConfig.DATE_FORMAT.format(new Date()), suffix,
                UUID.randomUUID().toString().replace("-", ""),
                suffix
        );
    }

}
