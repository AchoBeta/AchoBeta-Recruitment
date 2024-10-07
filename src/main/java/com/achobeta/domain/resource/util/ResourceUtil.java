package com.achobeta.domain.resource.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.enums.ResourceType;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.TimeUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

import static com.achobeta.domain.resource.enums.ResourceType.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 12:31
 */
public class ResourceUtil {

    public static void checkOriginalName(String originalName) {
        // 判断是否有非空字符以及是否有后缀
        if (!StringUtils.hasText(originalName) || !originalName.contains(".") || originalName.lastIndexOf(".") + 1 == originalName.length()) {
            throw new GlobalServiceException(String.format("资源名非法 %s", originalName), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static void checkSuffix(String suffix) {
        if (!StringUtils.hasText(suffix)|| suffix.length() <= 1 || !suffix.contains(".")) {
            throw new GlobalServiceException(String.format("后缀非法 %s", suffix), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static void checkExtension(String extension) {
        if (!StringUtils.hasText(extension)) {
            throw new GlobalServiceException(String.format("扩展名非法 %s", extension), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static void checkType(String contentType, ResourceType resourceType) {
        if(!contentType.startsWith(resourceType.getContentTypeSuffix())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_TYPE_NOT_MATCH);
        }
    }

    public static void checkImage(String contentType) {
        checkType(contentType, IMAGE);
    }

    public static void checkVideo(String contentType) {
        checkType(contentType, VIDEO);
    }

    public static void checkText(String contentType) {
        checkType(contentType, TEXT);
    }

    public static void checkSheet(byte[] bytes) {
        if(!ExcelUtil.isSheet(bytes)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_TYPE_NOT_MATCH);
        }
    }

    public static String getOriginalName(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        checkOriginalName(originalName);
        return originalName;
    }

    public static String getFileNameExcludeSuffix(String originalName) {
        checkOriginalName(originalName);
        return originalName.substring(0, originalName.lastIndexOf("."));
    }

    public static String getSuffix(String originalName) {
        checkOriginalName(originalName);
        return originalName.substring(originalName.lastIndexOf("."));
    }

    public static String getExtension(String originalName) {
        return getSuffix(originalName).substring(1);
    }

    public static String getSimpleFileName(String suffix) {
        checkSuffix(suffix);
        return UUID.randomUUID().toString().replace("-", "") + suffix;
    }

    public static String getFileNameBySuffix(String filename, String suffix) {
        checkSuffix(suffix);
        return StringUtils.hasText(filename) ? filename + suffix : getSimpleFileName(suffix);
    }

    public static String getFileNameByExtension(String filename, String extension) {
        checkExtension(extension);
        return getFileNameBySuffix(filename, "." + extension);
    }

    public static String getUniqueFileName(Long userId, String suffix) {
        return String.format(
                "%s/%d/%s/%s",
                TimeUtil.getDate(new Date()), userId, suffix,
                getSimpleFileName(suffix)
        );
    }

}
