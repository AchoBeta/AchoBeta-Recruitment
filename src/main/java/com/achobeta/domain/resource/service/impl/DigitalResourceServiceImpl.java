package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.base.BasePageResult;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.converter.DigitalResourceConverter;
import com.achobeta.domain.resource.model.dao.mapper.DigitalResourceMapper;
import com.achobeta.domain.resource.model.dto.ResourceQueryDTO;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.ResourceQueryVO;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.SnowflakeIdGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【digital_resource(资源表)】的数据库操作Service实现
* @createDate 2024-09-22 23:08:11
*/
@Service
@RequiredArgsConstructor
public class DigitalResourceServiceImpl extends ServiceImpl<DigitalResourceMapper, DigitalResource>
    implements DigitalResourceService{

    private final SnowflakeIdGenerator resourceCodeGenerator;

    @Override
    public DigitalResource getResourceByCode(Long code) {
        return this.lambdaQuery()
                .eq(DigitalResource::getCode, code)
                .oneOpt()
                .orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_NOT_EXISTS));
    }

    @Override
    public ResourceQueryVO queryResources(ResourceQueryDTO resourceQueryDTO) {
        // 解析分页参数获取 page
        IPage<DigitalResource> page = DigitalResourceConverter.INSTANCE
                .resourceQueryDTOToBasePageQuery(resourceQueryDTO)
                .toMpPage();

        // 分页
        IPage<DigitalResource> resourceIPage = this.page(page);
        // 封装
        BasePageResult<DigitalResource> pageResult = BasePageResult.of(resourceIPage);
        // 转化
        return DigitalResourceConverter.INSTANCE.basePageResultToResourceQueryVO(pageResult);
    }

    @Override
    public Long createResource(DigitalResource digitalResource) {
        return Optional.ofNullable(digitalResource)
                .map(resource -> {
                    // 生成一个雪花数字
                    Long code = resourceCodeGenerator.nextId();
                    resource.setCode(code);
                    this.save(resource);
                    return code;
                }).orElse(null);
    }

    @Override
    @Transactional
    public List<Long> createResourceBatch(List<DigitalResource> resourceList) {
        if(CollectionUtils.isEmpty(resourceList)) {
            return new ArrayList<>();
        }
        List<Long> codeList = new ArrayList<>();
        resourceList.stream()
                .filter(Objects::nonNull)
                .forEach(resource -> {
                    Long code = resourceCodeGenerator.nextId();
                    codeList.add(code);
                    resource.setCode(code);
                });
        this.saveBatch(resourceList);
        return codeList;
    }

    @Override
    public void updateAccessLevel(Long id, ResourceAccessLevel level) {
        DigitalResource digitalResource = new DigitalResource();
        digitalResource.setAccessLevel(level);
        this.lambdaUpdate()
                .eq(DigitalResource::getId, id)
                .update(digitalResource)
        ;
    }

    @Override
    public void removeDigitalResource(Long id) {
        lambdaUpdate()
                .eq(DigitalResource::getId, id)
                .remove();
    }
}




