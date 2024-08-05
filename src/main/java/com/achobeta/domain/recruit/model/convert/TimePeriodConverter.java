package com.achobeta.domain.recruit.model.convert;

import com.achobeta.domain.recruit.model.vo.TimePeriodCountVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-28
 * Time: 23:04
 */
@Mapper
public interface TimePeriodConverter {

    TimePeriodConverter INSTANCE = Mappers.getMapper(TimePeriodConverter.class);

    @Mapping(target = "count", expression = "java(0)")
    TimePeriodCountVO timePeriodVOToCountVO(TimePeriodVO timePeriodVO);

}
