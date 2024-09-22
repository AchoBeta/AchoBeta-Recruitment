package com.achobeta.domain.paper.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.paper.model.dto.PaperQueryDTO;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.PaperQueryVO;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.achobeta.domain.question.model.dto.QuestionQueryDTO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionQueryVO;
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

    BasePageQuery paperQueryDTOToBasePageQuery(PaperQueryDTO paperQueryDTO);

    PaperQueryVO basePageResultToPaperQueryVO(BasePageResult<QuestionPaper> basePageResult);

}
