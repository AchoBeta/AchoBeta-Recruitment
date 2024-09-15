package com.achobeta.domain.recruit.model.converter;

import com.achobeta.domain.recruit.model.entity.RecruitmentBatch;
import com.achobeta.domain.recruit.model.vo.RecruitmentBatchVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 14:11
 */
@Mapper
public interface RecruitmentBatchConverter {

    RecruitmentBatchConverter INSTANCE = Mappers.getMapper(RecruitmentBatchConverter.class);

    RecruitmentBatchVO recruitmentBatchToRecruitmentBatchVO(RecruitmentBatch recruitmentBatch);

    List<RecruitmentBatchVO> recruitmentBatchListToRecruitmentBatchVOList(List<RecruitmentBatch> recruitmentBatchList);

}
