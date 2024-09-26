package com.achobeta.domain.resource.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.dto.ResourceQueryDTO;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.DigitalResourceVO;
import com.achobeta.domain.resource.model.vo.ResourceAccessLevelVO;
import com.achobeta.domain.resource.model.vo.ResourceQueryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 2:06
 */
@Mapper
public interface DigitalResourceConverter {

    DigitalResourceConverter INSTANCE = Mappers.getMapper(DigitalResourceConverter.class);

    BasePageQuery resourceQueryDTOToBasePageQuery(ResourceQueryDTO resourceQueryDTO);

    ResourceQueryVO basePageResultToResourceQueryVO(BasePageResult<DigitalResource> basePageResult);

    List<DigitalResourceVO> digitalResourceListDigitalResourceVOList(List<DigitalResource> digitalResourceList);

    List<ResourceAccessLevelVO> levelListToLevelVOList(List<ResourceAccessLevel> resourceAccessLevelList);
}
