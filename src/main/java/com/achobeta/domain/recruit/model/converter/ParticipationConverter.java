package com.achobeta.domain.recruit.model.converter;

import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.vo.ParticipationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-28
 * Time: 23:17
 */
@Mapper
public interface ParticipationConverter {

    ParticipationConverter INSTANCE = Mappers.getMapper(ParticipationConverter.class);

    ParticipationVO activityParticipationToParticipationVO(ActivityParticipation activityParticipation);

}
