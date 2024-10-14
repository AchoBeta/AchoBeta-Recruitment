package com.achobeta.domain.schedule.model.dao.mapper;

import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.schedule.model.entity.InterviewSchedule;
import com.achobeta.domain.schedule.model.vo.ParticipationScheduleVO;
import com.achobeta.domain.schedule.model.vo.ScheduleDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【interview_schedule(面试预约表)】的数据库操作Mapper
* @createDate 2024-07-25 22:34:52
* @Entity com.achobeta.domain.interview.model.enity.InterviewSchedule
*/
public interface InterviewScheduleMapper extends BaseMapper<InterviewSchedule> {

    List<ScheduleDetailVO> getInterviewScheduleList(@Param("managerId") Long managerId, @Param("condition") InterviewConditionDTO interviewConditionDTO);

    List<ParticipationScheduleVO> getSituationsByActId(@Param("actId") Long actId);

    ParticipationScheduleVO getSituationsByParticipationId(@Param("participationId") Long participationId);

    ScheduleDetailVO getInterviewerScheduleDetail(@Param("scheduleId") Long scheduleId);

}




