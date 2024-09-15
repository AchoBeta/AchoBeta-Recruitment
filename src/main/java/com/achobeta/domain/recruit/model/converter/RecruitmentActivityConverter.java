package com.achobeta.domain.recruit.model.converter;

import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.model.vo.RecruitmentActivityVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-28
 * Time: 23:25
 */
@Mapper
public interface RecruitmentActivityConverter {

    RecruitmentActivityConverter INSTANCE = Mappers.getMapper(RecruitmentActivityConverter.class);

    RecruitmentActivityVO recruitmentActivityToRecruitmentActivityVO(RecruitmentActivity recruitmentActivity);

    List<RecruitmentActivityVO> recruitmentActivityListToRecruitmentActivityVOList(List<RecruitmentActivity> recruitmentActivityList);

}
