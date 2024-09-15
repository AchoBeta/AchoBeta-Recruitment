package com.achobeta.domain.paper.model.converter;

import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 14:22
 */
@Mapper
public interface QuestionConverter {

    QuestionConverter INSTANCE = Mappers.getMapper(QuestionConverter.class);

    List<QuestionVO> questionListToQuestionVOList(List<Question> questionList);

}

