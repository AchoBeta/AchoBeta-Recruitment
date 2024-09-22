package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.access.factory.AccessStrategyFactory;
import com.achobeta.domain.resource.access.strategy.ResourceAccessStrategy;
import com.achobeta.domain.resource.engine.MinioEngine;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.DigitalResourceVO;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.exception.GlobalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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

    private final MinioEngine minioEngine;

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
    public Long upload(Long userId, MultipartFile file) {
        String fileName = minioEngine.upload(file);
        return digitalResourceService.createResource(userId, fileName);
    }

    @Override
    @Transactional
    public List<Long> uploadList(Long userId, List<MultipartFile> fileList) {
        List<String> fileNameList = fileList.stream()
                .map(minioEngine::upload)
                .toList();
        return digitalResourceService.createResourceBatch(userId, fileNameList);
    }

    @Override
    public void setAccessLevel(Long id, ResourceAccessLevel level) {
        digitalResourceService.updateAccessLevel(id, level);
    }

    @Override
    public void remove(Long code) {
        DigitalResource digitalResource = checkAndGetResource(code, ResourceAccessLevel.USER_ACCESS);
        minioEngine.remove(digitalResource.getFileName());
        digitalResourceService.removeDigitalResource(digitalResource.getId());
    }
}
