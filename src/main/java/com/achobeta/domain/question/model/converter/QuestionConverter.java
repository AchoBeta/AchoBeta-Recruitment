package com.achobeta.domain.question.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.question.model.dto.QuestionDTO;
import com.achobeta.domain.question.model.dto.QuestionLibraryDTO;
import com.achobeta.domain.question.model.dto.QuestionQueryDTO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.achobeta.domain.question.model.vo.QuestionQueryVO;
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

    List<QuestionLibrary> questionLibraryDTOListToQuestionLibraryList(List<QuestionLibraryDTO> questionLibraryDTOList);

    List<Question> questionDTOListToQuestionList(List<QuestionDTO> questionDTOList);

    BasePageQuery questionQueryDTOToBasePageQuery(QuestionQueryDTO questionQueryDTO);

    QuestionQueryVO basePageResultToQuestionQueryVO(BasePageResult<Question> basePageResult);

}

