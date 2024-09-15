package com.achobeta.domain.paper.model.converter;

import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 14:19
 */
@Mapper
public interface QuestionPaperConverter {

    QuestionPaperConverter INSTANCE = Mappers.getMapper(QuestionPaperConverter.class);

    List<QuestionPaperVO> questionPaperListToPaperVOList(List<QuestionPaper> questionPaperList);

}
