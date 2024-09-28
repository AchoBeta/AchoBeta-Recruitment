package com.achobeta.domain.schedule.model.converter;

import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.schedule.model.vo.ActivitySituationExcelTemplate;
import com.achobeta.domain.schedule.model.vo.ParticipationDetailVO;
import com.achobeta.domain.schedule.model.vo.ParticipationScheduleVO;
import com.achobeta.domain.schedule.model.vo.UserParticipationVO;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-28
 * Time: 23:12
 */
@Mapper
public interface SituationConverter {

    SituationConverter INSTANCE = Mappers.getMapper(SituationConverter.class);

    UserParticipationVO participationScheduleVOToUserParticipationVO(ParticipationScheduleVO participationScheduleVO);

    ParticipationDetailVO activityParticipationToParticipationDetailVO(ActivityParticipation activityParticipation);

    ActivitySituationExcelTemplate userParticipationVOToSituationExcelTemplate(SimpleStudentVO simpleStudentVO);
}
