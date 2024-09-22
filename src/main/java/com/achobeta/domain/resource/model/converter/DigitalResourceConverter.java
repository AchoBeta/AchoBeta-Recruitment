package com.achobeta.domain.resource.model.converter;

import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.DigitalResourceVO;
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

    List<DigitalResourceVO> digitalResourceListDigitalResourceVOList(List<DigitalResource> digitalResourceList);

}
