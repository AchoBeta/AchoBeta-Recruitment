package com.achobeta.domain.feedback.service;

import com.achobeta.common.base.BasePageResultEntity;
import com.achobeta.domain.feedback.model.dto.HandleFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.QueryUserOfFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.UserFeedbackDTO;
import com.achobeta.domain.feedback.model.vo.FeedbackMessageVO;
import com.achobeta.domain.feedback.model.vo.UserFeedbackVO;
import com.achobeta.domain.feedback.model.vo.UserPersonalFeedBackVO;
import com.achobeta.domain.feedback.model.entity.UserFeedback;
import com.achobeta.domain.message.model.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author cattleyuan
* @description 针对表【user_feedback】的数据库操作Service
* @createDate 2024-07-10 15:30:01
*/
public interface UserFeedbackService extends IService<UserFeedback> {

    List<UserPersonalFeedBackVO> getUserFeedbackList();

    void submitUserFeedback(UserFeedbackDTO userFeedbackDTO);

    BasePageResultEntity<UserFeedbackVO> queryUserOfFeedbackList(QueryUserOfFeedbackDTO queryUserOfFeedbackDTO);

    Long handleFeedbackOfUser(HandleFeedbackDTO handleFeedbackDTO);

    FeedbackMessageVO queryMessageOfFeedback(Message message);

    Message judgeMessageOfFeedbackIfExist(Long messageId);

}
