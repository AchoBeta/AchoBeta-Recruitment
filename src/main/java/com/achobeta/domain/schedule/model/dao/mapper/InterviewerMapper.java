package com.achobeta.domain.schedule.model.dao.mapper;

import com.achobeta.domain.schedule.model.entity.Interviewer;
import com.achobeta.domain.schedule.model.vo.ScheduleInterviewerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【interviewer(面试官表)】的数据库操作Mapper
* @createDate 2024-07-25 22:34:52
* @Entity com.achobeta.domain.interview.model.enity.Interviewer
*/
public interface InterviewerMapper extends BaseMapper<Interviewer> {

    List<ScheduleInterviewerVO> getInterviewersByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);


}




