package com.achobeta.domain.interview.model.dao.mapper;

import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewNoticeTemplate;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【interview(面试表)】的数据库操作Mapper
* @createDate 2024-08-05 23:45:13
* @Entity com.achobeta.domain.interview.model.enity.Interview
*/
public interface InterviewMapper extends BaseMapper<Interview> {

    List<InterviewVO> managerGetInterviewList(@Param("managerId") Long managerId, @Param("condition") InterviewConditionDTO interviewConditionDTO);

    List<InterviewVO> userGetInterviewList(@Param("userId") Long userId, @Param("condition") InterviewConditionDTO interviewConditionDTO);

    InterviewDetailVO getInterviewDetail(@Param("interviewId") Long interviewId);

}




