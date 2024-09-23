package com.achobeta.domain.resource.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.MediaUtil;
import org.apache.tika.Tika;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 12:31
 */
public class ResourceUtil {

    private final static Tika TIKA = new Tika();

    public static String getOriginalName(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        // 判断是否有非空字符以及是否有后缀
        if (!StringUtils.hasText(originalName) || !originalName.contains(".")) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
        return originalName;
    }

    public static String getFileNameSuffix(String originalName) {
        // 判断是否有非空字符以及是否有后缀
        if (!StringUtils.hasText(originalName) || !originalName.contains(".")) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
        return originalName.substring(originalName.lastIndexOf("."));
    }

    public static String getContentType(byte[] data, String suffix) {
        try(InputStream inputStream = MediaUtil.getInputStream(data)) {
            return TIKA.detect(inputStream, suffix);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static String getUniqueFileName(String suffix) {
        return UUID.randomUUID().toString().replace("-", "") + suffix;
    }

}
