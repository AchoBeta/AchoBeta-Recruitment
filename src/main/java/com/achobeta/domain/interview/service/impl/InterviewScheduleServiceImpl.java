package com.achobeta.domain.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.entity.InterviewSchedule;
import com.achobeta.domain.interview.service.InterviewScheduleService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewScheduleMapper;
import org.springframework.stereotype.Service;

/**
* @author 马拉圈
* @description 针对表【interview_schedule(面试时间预约表)】的数据库操作Service实现
* @createDate 2024-05-11 02:22:11
*/
@Service
public class InterviewScheduleServiceImpl extends ServiceImpl<InterviewScheduleMapper, InterviewSchedule>
    implements InterviewScheduleService{

}




