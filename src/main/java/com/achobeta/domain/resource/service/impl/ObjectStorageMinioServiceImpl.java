package com.achobeta.domain.resource.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.monio.engine.MinioEngine;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 10:32
 */
public class ObjectStorageMinioServiceImpl implements ObjectStorageService {

    private final MinioEngine minioEngine = SpringUtil.getBean(MinioEngine.class);

    @Override
    public String upload(MultipartFile file) {
        try {
            return minioEngine.upload(file);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_UPLOAD_FAILED);
        }
    }

    @Override
    public String getObjectUrl(String fileName) {
        try {
            return minioEngine.getObjectBaseUrl(fileName);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_GET_OBJECT_URL_FAILED);
        }
    }

    @Override
    public void preview(String fileName, HttpServletResponse response) {
        try {
            minioEngine.preview(fileName, response);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_PREVIEW_FAILED);
        }
    }

    @Override
    public void download(String downloadName, String fileName, HttpServletResponse response) {
        try {
            minioEngine.download(downloadName, fileName, response);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_DOWNLOAD_FAILED);
        }
    }

    @Override
    public void remove(String fileName) {
        try {
            minioEngine.remove(fileName);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_REMOVE_FAILED);
        }
    }
}
