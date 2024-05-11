package com.achobeta.domain.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.entity.InterviewComment;
import com.achobeta.domain.interview.service.InterviewCommentService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 马拉圈
* @description 针对表【interview_comment(面评表)】的数据库操作Service实现
* @createDate 2024-05-11 02:22:11
*/
@Service
public class InterviewCommentServiceImpl extends ServiceImpl<InterviewCommentMapper, InterviewComment>
    implements InterviewCommentService{

}




