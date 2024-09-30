package com.achobeta.domain.feedback.converter;


import com.achobeta.domain.feedback.model.dto.UserFeedbackDTO;
import com.achobeta.domain.feedback.model.entity.UserFeedback;
import com.achobeta.domain.feedback.model.vo.FeedbackMessageVO;
import com.achobeta.domain.feedback.model.vo.UserFeedbackVO;
import com.achobeta.domain.feedback.model.vo.UserPersonalFeedBackVO;
import com.achobeta.domain.message.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface FeedbackConverter {
    FeedbackConverter MESSAGE_CONVERTER=Mappers.getMapper(FeedbackConverter.class);

    List<UserPersonalFeedBackVO> userFeedbackPoToPersonalVoList(List<UserFeedback> userFeedbackList);

    UserFeedback userFeedbackDTOToPo(UserFeedbackDTO userFeedbackDTO);

    List<UserFeedbackVO> userFeedbackPoToVOList(List<UserFeedback> userFeedbackList);

    FeedbackMessageVO MessageOfFeedbackPoToVO(Message message);
}
