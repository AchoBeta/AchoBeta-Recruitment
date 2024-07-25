package com.achobeta.domain.interview.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.entity.Interviewer;
import com.achobeta.domain.interview.service.InterviewerService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interviewer(面试官表)】的数据库操作Service实现
* @createDate 2024-07-25 22:34:52
*/
@Service
@RequiredArgsConstructor
public class InterviewerServiceImpl extends ServiceImpl<InterviewerMapper, Interviewer>
    implements InterviewerService{

    @Override
    public Optional<Interviewer> getInterviewer(Long managerId, Long scheduleId) {
        return this.lambdaQuery()
                .eq(Interviewer::getManagerId, managerId)
                .eq(Interviewer::getScheduleId, scheduleId)
                .oneOpt();
    }

    @Override
    public List<Interviewer> getInterviewersByScheduleId(Long scheduleId) {
        return this.lambdaQuery()
                .eq(Interviewer::getScheduleId, scheduleId)
                .list();
    }

    @Override
    public Long createInterviewer(Long managerId, Long scheduleId) {
        return getInterviewer(managerId, scheduleId).orElseGet(() -> {
            Interviewer interviewer = new Interviewer();
            interviewer.setManagerId(managerId);
            interviewer.setScheduleId(scheduleId);
            this.save(interviewer);
            return interviewer;
        }).getId();
    }

    @Override
    public void removeInterviewersByScheduleId(Long scheduleId) {
        this.lambdaUpdate()
                .eq(Interviewer::getScheduleId, scheduleId)
                .remove();
    }

    @Override
    public void removeInterviewer(Long managerId, Long scheduleId) {
        this.lambdaUpdate()
                .eq(Interviewer::getManagerId, managerId)
                .eq(Interviewer::getScheduleId, scheduleId)
                .remove();
    }

    @Override
    public void checkInterviewerExists(Long managerId, Long scheduleId) {
        getInterviewer(managerId, scheduleId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEWER_NOT_EXISTS));
    }
}




