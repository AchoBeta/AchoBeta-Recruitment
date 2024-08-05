package com.achobeta.domain.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.enity.Interview;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewMapper;
import org.springframework.stereotype.Service;

/**
* @author 马拉圈
* @description 针对表【interview(面试表)】的数据库操作Service实现
* @createDate 2024-08-05 23:45:13
*/
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview>
    implements InterviewService{

}




