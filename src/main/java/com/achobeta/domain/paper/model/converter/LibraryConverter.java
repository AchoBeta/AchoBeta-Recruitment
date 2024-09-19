package com.achobeta.domain.paper.model.converter;

import com.achobeta.domain.paper.model.entity.QuestionPaperLibrary;
import com.achobeta.domain.paper.model.vo.PaperLibraryVO;
import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.achobeta.domain.question.model.vo.QuestionLibraryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 14:16
 */
@Mapper
public interface LibraryConverter {

    LibraryConverter INSTANCE = Mappers.getMapper(LibraryConverter.class);

    List<PaperLibraryVO> paperLibraryListToPaperLibraryVOList(List<QuestionPaperLibrary> paperLibraryList);

    List<QuestionLibraryVO> questionLibraryListToQuestionLibraryVOList(List<QuestionLibrary> questionLibraryList);

}
