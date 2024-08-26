package com.achobeta.domain.evaluate.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.evaluate.model.dao.mapper.InterviewCommentMapper;
import com.achobeta.domain.evaluate.model.entity.InterviewComment;
import com.achobeta.domain.evaluate.model.vo.InterviewCommentVO;
import com.achobeta.domain.evaluate.service.InterviewCommentService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview_comment(面试评论表)】的数据库操作Service实现
* @createDate 2024-08-07 01:34:40
*/
@Service
@RequiredArgsConstructor
public class InterviewCommentServiceImpl extends ServiceImpl<InterviewCommentMapper, InterviewComment>
    implements InterviewCommentService{

    private final InterviewCommentMapper interviewCommentMapper;

    @Override
    public Optional<InterviewComment> getInterviewComment(Long commentId) {
        return this.lambdaQuery()
                .eq(InterviewComment::getId, commentId)
                .oneOpt();
    }

    @Override
    public List<InterviewCommentVO> getCommentListByInterviewId(Long interviewId) {
        return interviewCommentMapper.getCommentListByInterviewId(interviewId);
    }

    @Override
    public Long commentInterview(Long interviewId, Long managerId, String content) {
        InterviewComment interviewComment = new InterviewComment();
        interviewComment.setInterviewId(interviewId);
        interviewComment.setManagerId(managerId);
        interviewComment.setContent(content);
        this.save(interviewComment);
        return interviewComment.getId();
    }

    @Override
    public void updateComment(Long commentId, String content) {
        this.lambdaUpdate()
                .eq(InterviewComment::getId, commentId)
                .set(InterviewComment::getContent, content)
                .update();
    }

    @Override
    public void removeComment(Long commentId) {
        this.lambdaUpdate()
                .eq(InterviewComment::getId, commentId)
                .remove();
    }

    @Override
    public InterviewComment checkAndGetInterviewCommentExists(Long commentId) {
        return getInterviewComment(commentId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_COMMENT_NOT_EXISTS));
    }
}




