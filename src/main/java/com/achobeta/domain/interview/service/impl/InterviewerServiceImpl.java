package com.achobeta.domain.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.entity.Interviewer;
import com.achobeta.domain.interview.service.InterviewerService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Long createInterviewer(Long managerId, Long scheduleId) {
        Interviewer interviewer = new Interviewer();
        interviewer.setManagerId(managerId);
        interviewer.setScheduleId(scheduleId);
        this.save(interviewer);
        return interviewer.getId();
    }
}




