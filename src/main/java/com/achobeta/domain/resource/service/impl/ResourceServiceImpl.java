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
    public String analyzeCode(Long code) {
        return checkAndGetResource(code).getFileName();
    }

    @Override
    public void download(String fileName, HttpServletResponse response) {
        objectStorageServiceFactory.load().download(fileName, response);
    }

    @Override
    public Long upload(Long userId, MultipartFile file) {
        String fileName = objectStorageServiceFactory.load().upload(file);
        return digitalResourceService.createResource(userId, fileName);
    }

    @Override
    @Transactional
    public List<Long> uploadList(Long userId, List<MultipartFile> fileList) {
        ObjectStorageService storageService = objectStorageServiceFactory.load();
        List<String> fileNameList = fileList.stream()
                .map(storageService::upload)
                .toList();
        return digitalResourceService.createResourceBatch(userId, fileNameList);
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
