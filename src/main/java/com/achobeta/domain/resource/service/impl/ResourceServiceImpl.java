package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.access.strategy.ResourceAccessStrategy;
import com.achobeta.domain.resource.factory.AccessStrategyFactory;
import com.achobeta.domain.resource.factory.ObjectStorageServiceFactory;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.resource.util.ResourceUtil;
import com.achobeta.exception.GlobalServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:17
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ResourceServiceImpl implements ResourceService {

    private final AccessStrategyFactory accessStrategyFactory;

    private final DigitalResourceService digitalResourceService;

    private final ObjectStorageServiceFactory objectStorageServiceFactory;

    @Override
    public DigitalResource checkAndGetResource(Long code) {
        DigitalResource resource = digitalResourceService.getResourceByCode(code);
        // 获取策略并判断是否可以访问
        ResourceAccessStrategy accessStrategy = accessStrategyFactory.getStrategy(resource.getAccessLevel());
        if (accessStrategy.isAccessible(resource)) {
            return resource;
        } else {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_CANNOT_BE_ACCESSED);
        }
    }

    @Override
    public DigitalResource checkAndGetResource(Long code, ResourceAccessLevel level) {
        DigitalResource resource = digitalResourceService.getResourceByCode(code);
        // 获取策略并判断是否可以访问
        ResourceAccessStrategy accessStrategy = accessStrategyFactory.getStrategy(level);
        if (accessStrategy.isAccessible(resource)) {
            return resource;
        } else {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_CANNOT_BE_ACCESSED);
        }
    }

    @Override
    public DigitalResource analyzeCode(Long code) {
        return checkAndGetResource(code);
    }

    @Override
    public void download(Long code, HttpServletResponse response) {
        DigitalResource resource = analyzeCode(code);
        objectStorageServiceFactory.load().download(resource.getOriginalName(), resource.getFileName(), response);
    }

    @Override
    public void preview(Long code, HttpServletResponse response) {
        DigitalResource resource = analyzeCode(code);
        objectStorageServiceFactory.load().preview(resource.getFileName(), response);
    }

    @Override
    public Long upload(Long userId, MultipartFile file) {
        DigitalResource resource = new DigitalResource();
        resource.setUserId(userId);
        resource.setOriginalName(ResourceUtil.getOriginalName(file));
        resource.setFileName(objectStorageServiceFactory.load().upload(file));
        return digitalResourceService.createResource(resource);
    }

    @Override
    @Transactional
    public List<Long> uploadList(Long userId, List<MultipartFile> fileList) {
        ObjectStorageService storageService = objectStorageServiceFactory.load();
        List<DigitalResource> resourceList = fileList.stream()
                .map(file -> {
                    DigitalResource resource = new DigitalResource();
                    resource.setUserId(userId);
                    resource.setOriginalName(ResourceUtil.getOriginalName(file));
                    resource.setFileName(storageService.upload(file));
                    return resource;
                })
                .toList();
        return digitalResourceService.createResourceBatch(resourceList);
    }

    @Override
    public void setAccessLevel(Long id, ResourceAccessLevel level) {
        digitalResourceService.updateAccessLevel(id, level);
    }

    @Override
    public void remove(Long code) {
        ObjectStorageService storageService = objectStorageServiceFactory.load();
        DigitalResource digitalResource = checkAndGetResource(code, ResourceAccessLevel.USER_ACCESS);
        storageService.remove(digitalResource.getFileName());
        digitalResourceService.removeDigitalResource(digitalResource.getId());
    }
}
