package com.achobeta.domain.resource.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.repository.MinioEngine;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.exception.GlobalServiceException;
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
    public String preview(String fileName) {
        try {
            return minioEngine.preview(fileName);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_PREVIEW_FAILED);
        }
    }

    @Override
    public void download(String fileName, HttpServletResponse res) {
        try {
            minioEngine.download(fileName, res);
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
