package com.achobeta.domain.evaluate.service;

import com.achobeta.domain.evaluate.model.entity.InterviewComment;
import com.achobeta.domain.evaluate.model.vo.InterviewCommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview_comment(面试评论表)】的数据库操作Service
* @createDate 2024-08-07 01:34:40
*/
public interface InterviewCommentService extends IService<InterviewComment> {

    // 查询 ------------------------------------------

    Optional<InterviewComment> getInterviewComment(Long commentId);

    List<InterviewCommentVO> getCommentListByInterviewId(Long interviewId);

    // 写入 ------------------------------------------

    Long commentInterview(Long interviewId, Long managerId, String content);

    void updateComment(Long commentId, String content);

    void removeComment(Long commentId);

    // 检查 ------------------------------------------

    InterviewComment checkAndGetInterviewCommentExists(Long commentId);

}
