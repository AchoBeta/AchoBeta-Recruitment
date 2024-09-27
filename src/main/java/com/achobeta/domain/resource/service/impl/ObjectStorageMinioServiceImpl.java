package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.config.UploadLimitProperties;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.monio.config.MinioConfig;
import com.achobeta.monio.engine.MinioBucketEngine;
import com.achobeta.monio.engine.MinioEngine;
import com.achobeta.monio.enums.MinioPolicyTemplateEnum;
import com.achobeta.monio.template.DefaultPolicyTemplate;
import com.achobeta.redis.cache.RedisCache;
import com.achobeta.template.engine.TextEngine;
import com.achobeta.util.ResourceUtil;
import com.achobeta.util.TimeUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 10:32
 */
public class ObjectStorageMinioServiceImpl implements ObjectStorageService, InitializingBean {

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private UploadLimitProperties uploadLimitProperties;

    @Resource
    private TextEngine textEngine;

    @Resource
    private MinioBucketEngine minioBucketEngine;

    @Resource
    private MinioEngine minioEngine;

    @Resource
    private RedisCache redisCache;

    @Override
    public String upload(Long userId, String originalName, byte[] bytes) {
        // 查询是否被封禁
        String blockKey = ResourceConstants.REDIS_USER_UPLOAD_BLOCK + userId;
        redisCache.getCacheObject(blockKey).ifPresent(date -> {
            long blockDDL = (long) date;
            // 理论上 blockDDL 是肯定小于等于当前时间的，因为大于的时候 redisKey 应该到期了
            // （但还是进行判断，如果出现大于的情况，则把本该过期的 key 删了）
            if(blockDDL <= System.currentTimeMillis()) {
                String message = String.format("用户当前无法上传资源，将于 %s 解封，如有异议请联系管理员！", TimeUtil.getDateTime(blockDDL));
                throw new GlobalServiceException(message, GlobalServiceStatusCode.RESOURCE_UPLOAD_BLOCKED);
            }else {
                redisCache.deleteObject(blockKey);
            }
        });
        // 进行上传的检测
        String redisKey = ResourceConstants.REDIS_USER_UPLOAD_LIMIT + userId;
        // 获得已上传的次数
        Integer times = (Integer) redisCache.getCacheObject(redisKey).orElseGet(() -> {
            redisCache.setCacheObject(redisKey, 0, uploadLimitProperties.getCycle(), uploadLimitProperties.getUnit());
            return 0;
        });
        // 判断次数是否达到上限
        if(uploadLimitProperties.getFrequency().compareTo(times) <= 0) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_UPLOAD_TOO_FREQUENT);
        }
        // 自增
        redisCache.incrementCacheNumber(redisKey);
        try {
            // 上传资源
            return minioEngine.upload(originalName, bytes);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_UPLOAD_FAILED);
        }
    }

    @Override
    public String upload(Long userId, MultipartFile file) {
        try {
            return upload(userId, ResourceUtil.getOriginalName(file), file.getBytes());
        } catch (GlobalServiceException e) {
            throw e;
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_UPLOAD_FAILED);
        }
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
    public void blockUser(Long userId, Long blockDDL) {
        String blockKey = ResourceConstants.REDIS_USER_UPLOAD_BLOCK + userId;
        long timeout = blockDDL - System.currentTimeMillis(); // 现在距离解封时间的时长
        if(timeout > 0) {
            redisCache.setCacheObject(blockKey, blockDDL, timeout, TimeUnit.MILLISECONDS);
        } else {
            // 设置为过去时，则直接封禁
            redisCache.deleteObject(blockKey);
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
