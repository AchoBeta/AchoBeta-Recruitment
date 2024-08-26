package com.achobeta.domain.evaluate.model.dao.mapper;

import com.achobeta.domain.evaluate.model.entity.InterviewComment;
import com.achobeta.domain.evaluate.model.vo.InterviewCommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【interview_comment(面试评论表)】的数据库操作Mapper
* @createDate 2024-08-07 01:34:40
* @Entity com.achobeta.domain.evaluate.model.enity.InterviewComment
*/
public interface InterviewCommentMapper extends BaseMapper<InterviewComment> {

    List<InterviewCommentVO> getCommentListByInterviewId(@Param("interviewId") Long interviewId);

}




