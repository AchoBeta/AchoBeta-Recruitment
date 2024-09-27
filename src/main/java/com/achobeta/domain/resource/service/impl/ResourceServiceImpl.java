package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.resource.access.strategy.ResourceAccessStrategy;
import com.achobeta.domain.resource.enums.ExcelTemplateEnum;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.factory.AccessStrategyFactory;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.users.service.UserService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.achobeta.domain.resource.constants.ResourceConstants.DEFAULT_RESOURCE_ACCESS_LEVEL;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:17
 */
@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ResourceServiceImpl implements ResourceService {

    private final AccessStrategyFactory accessStrategyFactory;

    private final DigitalResourceService digitalResourceService;

    private final ObjectStorageService objectStorageService;
    @Override
    public DigitalResource checkAndGetResource(Long code, ResourceAccessLevel level) {
        DigitalResource resource = digitalResourceService.getResourceByCode(code);
        // 获取策略并判断是否可以访问（level 取最高的那个）
        ResourceAccessStrategy accessStrategy = accessStrategyFactory.getStrategy(resource.getAccessLevel().and(level));
        if (accessStrategy.isAccessible(resource)) {
            return resource;
        } else {
            throw new GlobalServiceException(
                    String.format("尝试访问资源 %s，已阻拦", resource.getCode()),
                    GlobalServiceStatusCode.RESOURCE_CANNOT_BE_ACCESSED
            );
        }
    }

    @Override
    public DigitalResource analyzeCode(Long code) {
        return checkAndGetResource(code, null);
    }

    @Override
    public void download(Long code, HttpServletResponse response) {
        DigitalResource resource = analyzeCode(code);
        objectStorageService.download(resource.getOriginalName(), resource.getFileName(), response);
    }

    @Override
    public void preview(Long code, HttpServletResponse response) {
        DigitalResource resource = analyzeCode(code);
        objectStorageService.preview(resource.getFileName(), response);
    }

    @Override
    public byte[] load(Long code) {
        DigitalResource resource = analyzeCode(code);
        return objectStorageService.load(resource.getFileName());
    }

    @Override
    public void checkImage(Long code) {
        // 加载资源
        byte[] bytes = load(code);
        // 判断是否为图片
        ResourceUtil.checkImage(MediaUtil.getContentType(bytes));
    }

    @Override
    @Transactional
    public void checkAndRemoveImage(Long code, Long old) {
        if(!code.equals(old)) {
            checkImage(code);
            removeKindly(old);
        }
    }

    @Override
    public String getSystemUrl(HttpServletRequest request, Long code) {
        String baseUrl = HttpServletUtil.getBaseUrl(request, "/api/v1/resource/download", "/{code}");
        return HttpRequestUtil.buildUrl(baseUrl, null, code);
    }

    @Override
    public String gerObjectUrl(Long code, Boolean hidden) {
        DigitalResource resource = analyzeCode(code);
        return objectStorageService.getObjectUrl(resource.getFileName(), hidden);
    }

    @Override
    public Long upload(Long userId, MultipartFile file) {
        return upload(userId, file, DEFAULT_RESOURCE_ACCESS_LEVEL);
    }

    @Override
    public Long upload(Long userId, MultipartFile file, ResourceAccessLevel level) {
        DigitalResource resource = new DigitalResource();
        resource.setUserId(userId);
        resource.setOriginalName(ResourceUtil.getOriginalName(file));
        resource.setFileName(objectStorageService.upload(userId, file));
        resource.setAccessLevel(level);
        return digitalResourceService.createResource(resource);
    }

    @Override
    public Long upload(Long userId, String originalName, byte[] data, ResourceAccessLevel level) {
        DigitalResource resource = new DigitalResource();
        resource.setUserId(userId);
        resource.setOriginalName(originalName);
        resource.setFileName(objectStorageService.upload(userId, originalName, data));
        resource.setAccessLevel(level);
        return digitalResourceService.createResource(resource);
    }

    @Override
    public <T, E> Long uploadExcel(Long managerId, ExcelTemplateEnum excelTemplateEnum, Class<T> clazz, List<E> data, ResourceAccessLevel level) {
        // 获取数据
        byte[] bytes = ExcelUtil.exportXlsxFile(excelTemplateEnum.getTitle(), excelTemplateEnum.getSheetName(), clazz, data);
        // 上传对象存储服务器
        return upload(managerId, excelTemplateEnum.getOriginalName(), bytes, level);
    }

    @Override
    @Transactional
    public List<Long> uploadList(Long userId, List<MultipartFile> fileList) {
        ObjectStorageService storageService = objectStorageService;
        List<DigitalResource> resourceList = fileList.stream()
                .map(file -> {
                    DigitalResource resource = new DigitalResource();
                    resource.setUserId(userId);
                    resource.setAccessLevel(DEFAULT_RESOURCE_ACCESS_LEVEL);
                    resource.setOriginalName(ResourceUtil.getOriginalName(file));
                    resource.setFileName(storageService.upload(userId, file));
                    return resource;
                })
                .toList();
        return digitalResourceService.createResourceBatch(resourceList);
    }

    @Override
    public void setAccessLevel(Long id, ResourceAccessLevel level) {
        digitalResourceService.setAccessLevel(id, level);
    }

    @Override
    @Transactional
    public void remove(Long code) {
        ObjectStorageService storageService = objectStorageService;
        // 若权限小于 USER_ACCESS 就按 USER_ACCESS 权限
        DigitalResource resource = checkAndGetResource(code, DEFAULT_RESOURCE_ACCESS_LEVEL);
        storageService.remove(resource.getFileName());
        digitalResourceService.removeDigitalResource(resource.getId());
    }

    @Override
    @Transactional
    public void removeKindly(Long code) {
        try {
            if(Objects.nonNull(code)) {
                remove(code);
            }
        } catch (GlobalServiceException e) {
            log.warn("尝试删除 code： {}，失败：{} {}", code, e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    public void blockUser(Long userId, Long date) {
        objectStorageService.blockUser(userId, date);
    }
}
