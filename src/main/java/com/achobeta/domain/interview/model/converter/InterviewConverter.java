package com.achobeta.domain.interview.model.converter;

import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 0:19
 */
@Mapper
public interface InterviewConverter {

    InterviewConverter INSTANCE = Mappers.getMapper(InterviewConverter.class);

    @Mapping(target = "status", expression = "java(com.achobeta.common.enums.InterviewStatusEnum.NOT_STARTED)")
    Interview interviewCreateDTOtoInterview(InterviewCreateDTO interviewCreateDTO);

    Interview interviewUpdateDTOtoInterview(InterviewUpdateDTO interviewUpdateDTO);

    List<InterviewVO> interviewListToInterviewVoList(List<Interview> interviewList);

}
