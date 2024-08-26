package com.achobeta.domain.evaluate.model.converter;

import com.achobeta.domain.evaluate.model.dto.InterviewSummaryDTO;
import com.achobeta.domain.evaluate.model.entity.InterviewSummary;
import com.achobeta.domain.evaluate.model.vo.InterviewSummaryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:52
 */
@Mapper
public interface InterviewSummaryConverter {

    InterviewSummaryConverter INSTANCE = Mappers.getMapper(InterviewSummaryConverter.class);

    InterviewSummary interviewSummaryDTOToInterviewSummary(InterviewSummaryDTO interviewSummaryDTO);

    InterviewSummaryVO interviewSummaryToInterviewSummaryVO(InterviewSummary interviewSummary);

}
