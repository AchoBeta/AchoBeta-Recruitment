package com.achobeta.domain.interview.service;

import com.achobeta.domain.interview.model.entity.Interviewer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interviewer(面试官表)】的数据库操作Service
* @createDate 2024-07-25 22:34:52
*/
public interface InterviewerService extends IService<Interviewer> {

    // 查询 ------------------------------------------

    Optional<Interviewer> getInterviewer(Long managerId, Long scheduleId);

    List<Interviewer> getInterviewersByScheduleId(Long scheduleId);

    // 写入 ------------------------------------------

    Long createInterviewer(Long managerId, Long scheduleId);

    void removeInterviewersByScheduleId(Long scheduleId);

    void removeInterviewer(Long managerId, Long scheduleId);

    // 检测 ------------------------------------------

    void checkInterviewerExists(Long managerId, Long scheduleId);

}
