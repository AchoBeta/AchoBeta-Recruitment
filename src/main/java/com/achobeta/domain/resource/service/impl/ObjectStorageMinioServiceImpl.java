package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.domain.resource.util.ResourceUtil;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.monio.config.MinioConfig;
import com.achobeta.monio.engine.MinioBucketEngine;
import com.achobeta.monio.engine.MinioEngine;
import com.achobeta.monio.enums.MinioPolicyTemplateEnum;
import com.achobeta.monio.template.DefaultPolicyTemplate;
import com.achobeta.template.engine.TextEngine;
import com.achobeta.util.MediaUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 10:32
 */

/**
 * 本服务由 SPI 加载，故必须提供无参构造方法
 */
public class ObjectStorageMinioServiceImpl implements ObjectStorageService, InitializingBean {

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private TextEngine textEngine;

    @Resource
    private MinioBucketEngine minioBucketEngine;

    @Resource
    private MinioEngine minioEngine;

    @Override
    public String upload(Long userId, String originalName, byte[] bytes) {
        try {
            // 上传资源
            String suffix = ResourceUtil.getSuffix(originalName);
            String uniqueFileName = ResourceUtil.getUniqueFileName(userId, suffix);
            minioEngine.upload(uniqueFileName, bytes);
            return uniqueFileName;
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_UPLOAD_FAILED);
        }
    }

    @Override
    public String upload(Long userId, MultipartFile file) {
        return upload(userId, ResourceUtil.getOriginalName(file), MediaUtil.getBytes(file));
    }

    @Override
    public String getObjectUrl(String fileName, Boolean hidden) {
        try {
            return minioEngine.getObjectUrl(fileName, hidden);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_GET_OBJECT_URL_FAILED);
        }
    }

    @Override
    public byte[] load(String fileName) {
        try {
            return minioEngine.load(fileName);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_LOAD_FAILED);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        // 如果不存在，则初始化桶
        String bucketName = minioConfig.getBucketName();
        // 设置规则：所有人都能读（否则就只能获取）
        DefaultPolicyTemplate policyTemplate = DefaultPolicyTemplate.builder()
                .bucketName(bucketName)
                .build();
        String policy = textEngine.builder()
                .append(MinioPolicyTemplateEnum.ALLOW_ALL_GET.getTemplate(), policyTemplate)
                .build();
        minioBucketEngine.tryMakeBucket(bucketName, policy);
    }
}
