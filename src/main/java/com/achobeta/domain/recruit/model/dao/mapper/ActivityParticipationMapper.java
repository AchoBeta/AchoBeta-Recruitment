package com.achobeta.domain.recruit.model.dao.mapper;

import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.vo.ParticipationPeriodVO;
import com.achobeta.domain.recruit.model.vo.ParticipationQuestionVO;
import com.achobeta.domain.recruit.model.vo.QuestionAnswerVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【activity_participation(“活动参与”表)】的数据库操作Mapper
* @createDate 2024-07-06 12:33:02
* @Entity com.achobeta.domain.recruit.model.entity.ActivityParticipation
*/
public interface ActivityParticipationMapper extends BaseMapper<ActivityParticipation> {

    List<QuestionAnswerVO> getQuestions(@Param("participationId") Long participationId);

    List<TimePeriodVO> getPeriods(@Param("participationId") Long participationId);

    List<ParticipationPeriodVO> getParticipationPeriods(@Param("participationIds") List<Long> participationIds);

    List<ParticipationQuestionVO> getParticipationQuestions(@Param("participationIds") List<Long> participationIds);
}




