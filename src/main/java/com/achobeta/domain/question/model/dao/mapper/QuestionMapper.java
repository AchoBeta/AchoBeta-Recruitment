package com.achobeta.domain.question.model.dao.mapper;

import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【question(问题表)】的数据库操作Mapper
* @createDate 2024-07-05 20:03:35
* @Entity com.achobeta.domain.question.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    List<QuestionVO> getQuestionsByLibId(@Param("libId") Long libId);

}




