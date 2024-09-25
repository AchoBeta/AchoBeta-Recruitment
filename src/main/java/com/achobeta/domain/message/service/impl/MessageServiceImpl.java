package com.achobeta.domain.message.service.impl;

import com.achobeta.common.base.BasePageResultEntity;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.message.converter.MessageConverter;
import com.achobeta.domain.message.handler.ext.MessageSendWithEmailHandler;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.EmailSendDTO;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.dto.StuOfMessageVO;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.student.model.converter.StuResumeConverter;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;

import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.RedisCache;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.domain.message.model.dao.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author cattleyuan
 * @description 针对表【message(“活动参与”表)】的数据库操作Service实现
 * @createDate 2024-08-11 22:20:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    private final StuResumeService stuResumeService;
    private final StuResumeConverter stuResumeConverter;
    private final MessageConverter messageConverter;
    private final MessageSendWithEmailHandler messageSendWithEmailHandler;

    private final RedisCache redisCache;

    @Override
    public BasePageResultEntity<StuOfMessageVO> queryStuListByCondition(QueryStuListDTO queryStuDTO) {


        //查询分页返回结果
        Page<StuResume> pageResult = getStuResumePage(queryStuDTO);

        //封装返回结果
        BasePageResultEntity<StuOfMessageVO> PageResultEntity = getStuOfMessagePageResultEntity(pageResult);

        return PageResultEntity;
    }

 /*   @Override
    public void sendMessage(MessageContentDTO messageContentBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {

        MessageContentVO messageContentVO = messageConverter.messageContentDTOToVO(messageContentBody);

        webSocketSet.stream()
                .filter(server -> !messageContentBody.getStuInfoSendList().stream().map(data -> data.getUserId()).toList().contains(server.getUserId()))
                .forEach(messageReceiveServer -> {
                    *//*姓名替换*//*
                    *//*messageContentVO.getContent().replace("$", )*//*

                    //消息内容
                    String messageText = JSON.toJSONString(messageContentVO);

                    //消息发送
                    messageReceiveServer.getSession().getAsyncRemote().sendText(messageText);


                });
        log.info("消息发送成功");
    }*/

    @Override
    public void sendMessage(MessageContentDTO messageContentBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {
        MessageContentVO messageContentVO = messageConverter.messageContentDTOToVO(messageContentBody);

        // 获取需要排除的用户ID列表
        List<Long> excludedUserIds = messageContentBody.getStuInfoSendList().stream()
                .map(data -> data.getUserId())
                .collect(Collectors.toList());

        // 发送消息
        webSocketSet.stream()
                .filter(server -> !excludedUserIds.contains(server.getUserId()))
                .forEach(messageReceiveServer -> {
                    String messageText = JSON.toJSONString(messageContentVO);

                    // 使用 synchronized 确保同一个会话的发送是串行的
                    synchronized (messageReceiveServer) {
                        try {
                            if (messageReceiveServer.getSession().isOpen()) {
                                messageReceiveServer.getSession().getAsyncRemote().sendText(messageText, result -> {
                                    if (result.getException() != null) {
                                        log.error("用户id->{} 消息发送失败! 异常信息: {}", messageReceiveServer.getUserId(), result.getException().getMessage());
                                    } else {
                                        log.info("用户id->{} 消息发送成功!", messageReceiveServer.getUserId());
                                    }
                                });
                            }
                        } catch (Exception e) {
                            log.error("用户id->{} 消息发送过程中出现异常: {}", messageReceiveServer.getUserId(), e.getMessage());
                        }
                    }
                });

        log.info("消息发送请求已发起");
    }


    @Override
    public Long storeMessage(MessageContentDTO messageContent) {
        Long managerId = 1L;
       /* try {
            //获取当前管理员id
             managerId = BaseContext.getCurrentUser().getUserId();
        } catch (Exception e) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NOT_LOGIN);
        }*/

        //保存消息到数据库
        Message message = messageConverter.messsageContentDTOToPo(messageContent);
        message.setManagerId(managerId);

        messageContent.getStuInfoSendList().stream().forEach(stu -> {
            message.setUserId(stu.getUserId());
            this.save(message);
        });
        return message.getId();
        //保存消息到缓存
      /*  MessageContentVO messageContentVO = messageConverter.messageContentDTOToVO(messageContent);
        messageContent.getStuInfoSendList()
                .stream()
                .forEach(receiverId -> {
                            redisCache.setCacheList(MESSAGE_CACHE_NAME + receiverId, List.of(messageContentVO));
                            redisCache.setCacheList(MESSAGE_CACHE_NAME + managerId, List.of(messageContentVO));
                        }
                );*/

    }

    @Override
    public List<MessageContentVO> getMessageListofUser() {
        Long userId = BaseContext.getCurrentUser().getUserId();
        //缓存消息列表
        /*Optional<List<MessageContentVO>> messageOptional = redisCache.getCacheList(MESSAGE_CACHE_NAME + userId, MessageContentVO.class);

        if (!messageOptional.isEmpty())
            return messageOptional.get();*/
        //查询并封装返回结果
        List<Message> messageList = lambdaQuery().eq(Message::getUserId, userId).list();
        List<MessageContentVO> messageContentVOList = messageConverter.messageContentPoToVOList(messageList);

        return messageContentVOList;
    }

    @Override
    public void sendMessageByEmail(EmailSendDTO emailSendDTO) {
        emailSendDTO.getStuInfoSendList().stream().forEach(stu-> messageSendWithEmailHandler.buildEmailAndSend(stu.getEmail(),emailSendDTO.getTittle(),emailSendDTO.getContent(),stu.getStuName()));
        log.info("邮箱发送成功!");
    }


    private BasePageResultEntity<StuOfMessageVO> getStuOfMessagePageResultEntity(Page<StuResume> pageResult) {
        //定义返回结果对象
        BasePageResultEntity<StuOfMessageVO> PageResultEntity = new BasePageResultEntity<>();

        //数据处理
        List<StuResume> stuResumeList = pageResult.getRecords();
        List<StuOfMessageVO> stuOfMessageVOList = stuResumeConverter.stuResumeListToStuMessageVOList(stuResumeList);

        //封装结果集
        PageResultEntity.setRecords(stuOfMessageVOList);
        PageResultEntity.setPages((int) pageResult.getPages());
        PageResultEntity.setTotal(pageResult.getTotal());
        return PageResultEntity;
    }

    private Page<StuResume> getStuResumePage(QueryStuListDTO queryStuDTO) {
        //封装分页查询条件
        Page<StuResume> page = Page.of(queryStuDTO.getPageNo(), queryStuDTO.getPageSize());
        //根据查询条件获取分页数据集
        Page<StuResume> pageResult = stuResumeService.lambdaQuery()
                .like(queryStuDTO.getName() != null && !queryStuDTO.getName().isBlank(), StuResume::getName, queryStuDTO.getName())
                .eq(queryStuDTO.getBatchId() != null, StuResume::getBatchId, queryStuDTO.getBatchId())
                .eq(queryStuDTO.getGrade() != null, StuResume::getGrade, queryStuDTO.getGrade())
                .eq(queryStuDTO.getStatus() != null, StuResume::getStatus, queryStuDTO.getStatus())
                .page(page);
        return pageResult;
    }
}




