package com.achobeta.domain.question.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.question.model.dto.QuestionQueryDTO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionQueryVO;
import com.achobeta.domain.question.model.vo.QuestionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    BasePageQuery questionQueryDTOToBasePageQuery(QuestionQueryDTO questionQueryDTO);

    QuestionQueryVO basePageResultToQuestionQueryVO(BasePageResult<Question> basePageResult);

}

