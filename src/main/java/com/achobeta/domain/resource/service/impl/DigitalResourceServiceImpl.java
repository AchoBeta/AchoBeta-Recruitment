package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
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

import java.time.LocalDateTime;
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
                .map(resource -> {
                    // 以 updateTime 字段作为最近访问时间的标识
                    this.lambdaUpdate()
                            .eq(DigitalResource::getId, resource.getId())
                            .set(DigitalResource::getUpdateTime, LocalDateTime.now())
                            .update();
                    return resource;
                })
                .orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_NOT_EXISTS));
    }

    @Override
    public ResourceQueryVO queryResources(ResourceQueryDTO resourceQueryDTO) {
        // 解析分页参数获取 page
        IPage<DigitalResource> page = null;
        Long userId = null;
        ResourceAccessLevel level = null;
        if (Objects.isNull(resourceQueryDTO)) {
            page = new BasePageQuery().toMpPage();
        } else {
            page = DigitalResourceConverter.INSTANCE.resourceQueryDTOToBasePageQuery(resourceQueryDTO).toMpPage();
            userId = resourceQueryDTO.getUserId();
            level = Optional.ofNullable(resourceQueryDTO.getLevel())
                    .map(ResourceAccessLevel::get) // 这个时候就要求必须是有效的 level
                    .orElse(null);
        }
        // 分页
        IPage<DigitalResource> resourceIPage = this.lambdaQuery()
                .eq(Objects.nonNull(userId), DigitalResource::getUserId, userId)
                .eq(Objects.nonNull(level), DigitalResource::getAccessLevel, level)
                .page(page);
        // 封装
        BasePageResult<DigitalResource> pageResult = BasePageResult.of(resourceIPage);
        // 转化
        return DigitalResourceConverter.INSTANCE.basePageResultToResourceQueryVO(pageResult);
    }

    @Override
    public DigitalResource createResource(DigitalResource digitalResource) {
        // 生成一个雪花数字
        Long code = resourceCodeGenerator.nextId();
        digitalResource.setCode(code);
        this.save(digitalResource);
        return digitalResource;
    }

    @Override
    public void setAccessLevel(Long id, ResourceAccessLevel level) {
        DigitalResource digitalResource = new DigitalResource();
        digitalResource.setId(id);
        digitalResource.setAccessLevel(level);
        this.updateById(digitalResource);
    }

    @Override
    public void removeDigitalResource(Long id) {
        this.lambdaUpdate()
                .eq(DigitalResource::getId, id)
                .remove();
    }
}




