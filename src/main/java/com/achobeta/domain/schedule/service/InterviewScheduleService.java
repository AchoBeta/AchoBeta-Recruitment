package com.achobeta.domain.schedule.service;

import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.model.vo.InterviewReserveVO;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.schedule.model.entity.InterviewSchedule;
import com.achobeta.domain.schedule.model.vo.ParticipationDetailVO;
import com.achobeta.domain.schedule.model.vo.ScheduleDetailVO;
import com.achobeta.domain.schedule.model.vo.ScheduleResumeVO;
import com.achobeta.domain.schedule.model.vo.UserSituationVO;
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

    List<ScheduleResumeVO> getInterviewScheduleList(Long managerId, InterviewConditionDTO interviewConditionDTO);

    /**
     * @param actId 活动 id
     * @return 学生们参与情况，每一个学生包括：
     *  1. 学生的基础信息
     *  2. 学生时间段选择情况
     *  3. 学生面试预约情况
     *  并统计各个时间段选中次数
     */
    UserSituationVO getSituationsByActId(Long actId);

    ScheduleDetailVO getInterviewScheduleDetail(Long scheduleId);

    ParticipationDetailVO getDetailActivityParticipation(Long participationId);

    OnlineResourceVO printSituations(Long managerId, RecruitmentActivity activity, ResourceAccessLevel level, Boolean synchronous);

    InterviewReserveVO interviewReserveApply(Long scheduleId, String title, String mobile);

    // 写入 ------------------------------------------

    Long createInterviewSchedule(Long managerId, Long participationId, Long startTime, Long endTime);

    void removeInterviewSchedule(Long scheduleId);

    void exitInterview(Long managerId, Long scheduleId);

    void updateInterviewSchedule(Long scheduleId, Long startTime, Long endTime);

    // 检测 ------------------------------------------

    void checkInterviewScheduleExists(Long scheduleId);

    void checkScheduleReferenced(Long scheduleId);

}
