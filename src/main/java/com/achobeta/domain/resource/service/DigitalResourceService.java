package com.achobeta.domain.resource.service;

import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.dto.ResourceQueryDTO;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.DigitalResourceVO;
import com.achobeta.domain.resource.model.vo.ResourceQueryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【digital_resource(资源表)】的数据库操作Service
* @createDate 2024-09-22 23:08:11
*/
public interface DigitalResourceService extends IService<DigitalResource> {

    DigitalResource getResourceByCode(Long code);

    ResourceQueryVO queryResources(ResourceQueryDTO resourceQueryDTO);

    Long createResource(Long userId, String fileName);

    List<Long> createResourceBatch(Long userId, List<String> fileNameList);

    void updateAccessLevel(Long id, ResourceAccessLevel level);

    void removeDigitalResource(Long id);

}
