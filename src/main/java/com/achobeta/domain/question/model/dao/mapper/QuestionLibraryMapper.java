package com.achobeta.domain.question.model.dao.mapper;

import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 马拉圈
* @description 针对表【question_library(题库表)】的数据库操作Mapper
* @createDate 2024-07-05 20:03:35
* @Entity com.achobeta.domain.question.model.entity.QuestionLibrary
*/
public interface QuestionLibraryMapper extends BaseMapper<QuestionLibrary> {

    QuestionDetailVO getQuestionDetail(@Param("questionId") Long questionId);

}




