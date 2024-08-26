package com.achobeta.domain.evaluate.model.dao.mapper;

import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.evaluate.model.vo.InterviewQuestionAverageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【interview_question_score(面试题评分关联表)】的数据库操作Mapper
* @createDate 2024-08-07 02:02:23
* @Entity com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore
*/
public interface InterviewQuestionScoreMapper extends BaseMapper<InterviewQuestionScore> {

    List<InterviewQuestionAverageVO> getAverageQuestions(@Param("questionIds") List<Long> questionIds);

}




