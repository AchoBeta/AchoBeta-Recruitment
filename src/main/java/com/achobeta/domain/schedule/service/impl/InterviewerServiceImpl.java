package com.achobeta.domain.schedule.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.schedule.model.dao.mapper.InterviewerMapper;
import com.achobeta.domain.schedule.model.entity.Interviewer;
import com.achobeta.domain.schedule.model.vo.ScheduleInterviewerVO;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    private final InterviewerMapper interviewerMapper;

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
    public List<ScheduleInterviewerVO> getInterviewersByScheduleIds(List<Long> scheduleIds) {
        scheduleIds = ObjectUtil.distinctNonNullStream(scheduleIds).toList();
        if(CollectionUtils.isEmpty(scheduleIds)) {
            return new ArrayList<>();
        }
        return interviewerMapper.getInterviewersByScheduleIds(scheduleIds);
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




