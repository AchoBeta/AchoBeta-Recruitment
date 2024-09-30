package com.achobeta.domain.interview.model.converter;

import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.*;
import com.lark.oapi.service.vc.v1.model.Reserve;
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

    @Mapping(target = "status", expression = "java(InterviewStatus.NOT_STARTED)")
    Interview interviewCreateDTOtoInterview(InterviewCreateDTO interviewCreateDTO);

    @Mapping(target = "id", source = "interviewId")
    Interview interviewUpdateDTOtoInterview(InterviewUpdateDTO interviewUpdateDTO);

    List<InterviewVO> interviewListToInterviewVoList(List<Interview> interviewList);

    List<InterviewStatusVO> interviewStatusListToInterviewStatusVOList(List<InterviewStatus> interviewStatusList);

    List<InterviewEventVO> interviewEventListToInterviewEventVOList(List<InterviewEvent> interviewEventList);

    InterviewReserveVO feishuReserveToInterviewReserveVO(Reserve reserve);

    List<InterviewExcelTemplate> interviewVOListToInterviewExcelTemplateList(List<InterviewVO> interviewVOList);

}
