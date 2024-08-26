package com.achobeta.domain.evaluate.model.converter;

import com.achobeta.domain.evaluate.model.vo.InterviewPaperDetailVO;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 12:56
 */
@Mapper
public interface InterviewScoreConverter {

    InterviewScoreConverter INSTANCE = Mappers.getMapper(InterviewScoreConverter.class);

    InterviewPaperDetailVO questionPaperDetailVOToInterviewPaperDetailVO(QuestionPaperDetailVO questionPaperDetailVO);

}
