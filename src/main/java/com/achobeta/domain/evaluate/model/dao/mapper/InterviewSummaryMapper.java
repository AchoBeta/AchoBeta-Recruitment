package com.achobeta.domain.evaluate.model.dao.mapper;

import com.achobeta.domain.evaluate.model.entity.InterviewSummary;
import com.achobeta.domain.evaluate.model.vo.InterviewRankVO;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【interview_summary(面试总结表)】的数据库操作Mapper
* @createDate 2024-08-07 01:34:40
* @Entity com.achobeta.domain.evaluate.model.enity.InterviewSummary
*/
public interface InterviewSummaryMapper extends BaseMapper<InterviewSummary> {

    List<InterviewRankVO> getInterviewRankList(@Param("condition") InterviewConditionDTO interviewConditionDTO);

}




