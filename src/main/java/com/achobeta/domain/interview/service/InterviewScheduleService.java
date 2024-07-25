package com.achobeta.domain.interview.service;

import com.achobeta.domain.interview.model.entity.InterviewSchedule;
import com.achobeta.domain.interview.model.vo.ScheduleResumeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview_schedule(面试预约表)】的数据库操作Service
* @createDate 2024-07-25 22:34:52
*/
public interface InterviewScheduleService extends IService<InterviewSchedule> {

    // 查询 ------------------------------------------

    Optional<InterviewSchedule> getInterviewSchedule(Long scheduleId);

    List<ScheduleResumeVO> getInterviewScheduleList(Long managerId, Long actId);

    // 写入 ------------------------------------------

    Long createInterviewSchedule(Long managerId, Long participationId, Long startTime, Long endTime);

    void removeInterviewSchedule(Long scheduleId);

    void exitInterview(Long managerId, Long scheduleId);

    void updateInterviewSchedule(Long scheduleId, Long startTime, Long endTime);

    // 检测 ------------------------------------------

    void checkInterviewScheduleExists(Long scheduleId);

}
