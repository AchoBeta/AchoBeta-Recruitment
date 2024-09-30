package com.achobeta.domain.feedback.service.impl;

import com.achobeta.common.base.BasePageResultEntity;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.feedback.converter.FeedbackConverter;
import com.achobeta.domain.feedback.model.dao.mapper.UserFeedbackMapper;
import com.achobeta.domain.feedback.model.dto.HandleFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.QueryUserOfFeedbackDTO;
import com.achobeta.domain.feedback.model.dto.UserFeedbackDTO;
import com.achobeta.domain.feedback.model.entity.UserFeedback;
import com.achobeta.domain.feedback.model.vo.FeedbackMessageVO;
import com.achobeta.domain.feedback.model.vo.UserFeedbackVO;
import com.achobeta.domain.feedback.model.vo.UserPersonalFeedBackVO;
import com.achobeta.domain.feedback.service.UserFeedbackService;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.service.UserService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author cattleyuan
* @description 针对表【user_feedback】的数据库操作Service实现
* @createDate 2024-07-10 15:30:01
*/
@Service
@RequiredArgsConstructor
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback>
    implements UserFeedbackService{

    private final FeedbackConverter feedbackConverter;
    private final UserService userService;
    private final MessageService messageService;

    @Override
    public List<UserPersonalFeedBackVO> getUserFeedbackList() {
        long userId = BaseContext.getCurrentUser().getUserId();
        //根据用户id查询反馈列表
        List<UserFeedback> userFeedbackList = lambdaQuery().eq(UserFeedback::getUserId, userId).list();
        //构造返回实体
        List<UserPersonalFeedBackVO> userPersonalFeedBackVOList = feedbackConverter.userFeedbackPoToPersonalVoList(userFeedbackList);

        return userPersonalFeedBackVOList;
    }

    @Override
    public void submitUserFeedback(UserFeedbackDTO userFeedbackDTO) {
        //获取用户id
        long userId = BaseContext.getCurrentUser().getUserId();
        //构造反馈实体
        UserFeedback userFeedback=feedbackConverter.userFeedbackDTOToPo(userFeedbackDTO);
        userFeedback.setUserId(userId);
        //保存反馈
        save(userFeedback);
    }

    @Override
    public BasePageResultEntity<UserFeedbackVO> queryUserOfFeedbackList(QueryUserOfFeedbackDTO queryUserOfFeedbackDTO) {
        //分页查询用户反馈列表
        Page<UserFeedback> userFeedbackPage = queryUserFeedbackPage(queryUserOfFeedbackDTO);
        //将反馈列表转为VO对象
        List<UserFeedback> userFeedbackList = userFeedbackPage.getRecords();
        List<UserFeedbackVO> userFeedbackVOList=feedbackConverter.userFeedbackPoToVOList(userFeedbackList);

        //构建返回分页结果
        BasePageResultEntity<UserFeedbackVO> userFeedbackVOPageResult = buildUserFeedbackVOPageResult(userFeedbackPage, userFeedbackVOList);
        return userFeedbackVOPageResult;
    }

    @Override
    public Long handleFeedbackOfUser(HandleFeedbackDTO handleFeedbackDTO) {
        //查询用户反馈信息
        UserFeedback userFeedback = this.getById(handleFeedbackDTO.getFeedbackId());
        Optional.ofNullable(userFeedback).orElseThrow(()->new GlobalServiceException("不存在的用户反馈"));
        //处理用户反馈
        Long messageId = handleFeedback(handleFeedbackDTO, userFeedback);

        return messageId;
    }

    @Override
    public FeedbackMessageVO queryMessageOfFeedback(Message message) {

        Integer role = BaseContext.getCurrentUser().getRole();

        //学生用户只能查自己的反馈消息
        if (!(role.equals(UserTypeEnum.USER)&&message.getUserId().equals(BaseContext.getCurrentUser().getUserId())))
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);

        return getFeedbackMessageVO(message);

    }

    private FeedbackMessageVO getFeedbackMessageVO(Message message) {
        FeedbackMessageVO feedbackMessageVO=feedbackConverter.MessageOfFeedbackPoToVO(message);
        Optional<UserEntity> userEntity = userService.getUserById(message.getManagerId());
        userEntity.orElseThrow(()->new GlobalServiceException(GlobalServiceStatusCode.MESSAGE_HANDLER_NOT_EXIST));
        feedbackMessageVO.setManagerName(userEntity.get().getUsername());
        return feedbackMessageVO;
    }

    @Override
    public Message judgeMessageOfFeedbackIfExist(Long messageId) {
        Message message = messageService.getById(messageId);
        Optional.ofNullable(message).orElseThrow(()->new GlobalServiceException(GlobalServiceStatusCode.MESSAGE_NOT_EXIST));
        return message;
    }

    private Long handleFeedback(HandleFeedbackDTO handleFeedbackDTO, UserFeedback userFeedback) {
        Long messageId = messageService.storeMessage(handleFeedbackDTO.getMessageContentDTO());
        userFeedback.setMessageId(messageId);
        userFeedback.setIsHandle(Boolean.TRUE);
        //更新反馈消息
        this.updateById(userFeedback);
        return messageId;
    }

    private static BasePageResultEntity<UserFeedbackVO> buildUserFeedbackVOPageResult(Page<UserFeedback> userFeedbackPage, List<UserFeedbackVO> userFeedbackVOList) {
        BasePageResultEntity<UserFeedbackVO> userFeedbackVOPageResult = new BasePageResultEntity<>();
        userFeedbackVOPageResult.setTotal(userFeedbackPage.getTotal());
        userFeedbackVOPageResult.setRecords(userFeedbackVOList);
        userFeedbackVOPageResult.setPages((int) userFeedbackPage.getPages());
        return userFeedbackVOPageResult;
    }

    private Page<UserFeedback> queryUserFeedbackPage(QueryUserOfFeedbackDTO queryUserOfFeedbackDTO) {
        Page<UserFeedback> page = Page.of(queryUserOfFeedbackDTO.getPageNo(), queryUserOfFeedbackDTO.getPageSize());
        Page<UserFeedback> userFeedbackPage = lambdaQuery()
                .eq(queryUserOfFeedbackDTO.getIsHandle() != null, UserFeedback::getIsHandle, queryUserOfFeedbackDTO.getIsHandle() ? 1 : 0)
                .eq(queryUserOfFeedbackDTO.getBatchId() != null, UserFeedback::getBatchId, queryUserOfFeedbackDTO.getBatchId())
                .page(page);
        return userFeedbackPage;
    }
}




