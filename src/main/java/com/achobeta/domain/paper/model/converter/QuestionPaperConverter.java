package com.achobeta.domain.paper.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.paper.model.dto.PaperQueryDTO;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.PaperQueryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
