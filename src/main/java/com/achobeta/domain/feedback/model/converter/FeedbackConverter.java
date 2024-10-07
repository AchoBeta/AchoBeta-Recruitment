package com.achobeta.domain.feedback.model.converter;


import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.feedback.model.dto.QueryUserOfFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.UserFeedbackDTO;
import com.achobeta.domain.feedback.model.entity.UserFeedback;
import com.achobeta.domain.feedback.model.vo.FeedbackMessageVO;
import com.achobeta.domain.feedback.model.vo.QueryUserOfFeedbackVO;
import com.achobeta.domain.feedback.model.vo.UserPersonalFeedBackVO;
import com.achobeta.domain.message.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface FeedbackConverter {
    FeedbackConverter MESSAGE_CONVERTER = Mappers.getMapper(FeedbackConverter.class);

    List<UserPersonalFeedBackVO> userFeedbackPoToPersonalVoList(List<UserFeedback> userFeedbackList);

    UserFeedback userFeedbackDTOToPo(UserFeedbackDTO userFeedbackDTO);

    FeedbackMessageVO MessageOfFeedbackPoToVO(Message message);

    BasePageQuery queryUserOfFeedbackDTOToBasePageQuery(QueryUserOfFeedbackDTO queryUserOfFeedbackDTO);

    QueryUserOfFeedbackVO basePageResultToQueryUserOfFeedbackVO(BasePageResult<UserFeedback> basePageResult);

}
