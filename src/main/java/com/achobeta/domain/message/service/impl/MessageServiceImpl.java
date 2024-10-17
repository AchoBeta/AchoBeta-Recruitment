package com.achobeta.domain.message.service.impl;

import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.message.constants.AttachmentConstants;
import com.achobeta.domain.message.handler.ext.MessageSendWithEmailHandler;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.converter.MessageConverter;
import com.achobeta.domain.message.model.dao.mapper.MessageMapper;
import com.achobeta.domain.message.model.dto.*;
import com.achobeta.domain.message.model.entity.AttachmentFile;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.model.vo.QueryStuListVO;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.email.model.po.EmailAttachment;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

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
    private final MessageConverter messageConverter;
    private final ResourceService resourceService;
    private final MessageSendWithEmailHandler messageSendWithEmailHandler;


    @Override
    public QueryStuListVO queryStuListByCondition(QueryStuListDTO queryStuDTO) {
        IPage<StuResume> page = messageConverter.queryStuListDTOToBasePageQuery(queryStuDTO).toMpPage();
        //查询分页返回结果
        IPage<StuResume> pageResult = stuResumeService.lambdaQuery()
                .eq(Objects.nonNull(queryStuDTO.getBatchId()), StuResume::getBatchId, queryStuDTO.getBatchId())
                .eq(Objects.nonNull(queryStuDTO.getGrade()), StuResume::getGrade, queryStuDTO.getGrade())
                .eq(Objects.nonNull(queryStuDTO.getStatus()), StuResume::getStatus, queryStuDTO.getStatus())
                .page(page);
        //封装返回结果
        BasePageResult<StuResume> stuResumeBasePageResult = BasePageResult.of(pageResult);
        return messageConverter.basePageResultToQueryStuListVO(stuResumeBasePageResult);
    }

    @Override
    public List<StuBaseInfoDTO> queryStuList(Long batchId, List<Long> userIds) {
        List<StuResume> stuResumeList = stuResumeService.queryStuList(batchId, userIds);
        return messageConverter.stuResumeListToStuBaseInfoDTOList(stuResumeList);
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
                .map(StuBaseInfoDTO::getUserId)
                .toList();

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

        messageContent.getStuInfoSendList().forEach(stu -> {
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
    public List<MessageContentVO> getMessageListOfUser(Long userId) {
        //缓存消息列表
        /*Optional<List<MessageContentVO>> messageOptional = redisCache.getCacheList(MESSAGE_CACHE_NAME + userId, MessageContentVO.class);

        if (!messageOptional.isEmpty())
            return messageOptional.get();*/
        //查询并封装返回结果
        List<Message> messageList = lambdaQuery().eq(Message::getUserId, userId).list();
        return messageConverter.messageContentPoToVOList(messageList);
    }

    @Override
    public void sendMessageByEmail(Long managerId, EmailSendDTO emailSendDTO, List<MultipartFile> attachmentList) {
        //构造邮箱附件列表
        List<EmailAttachment> emailAttachmentList = CollectionUtils.isEmpty(attachmentList) ? Collections.emptyList() : attachmentList.stream().map(EmailAttachment::of).toList();
        //构造附件 url 列表
        List<AttachmentFile> attachmentInfoList = getAttachUrlList(managerId, attachmentList);
        // 发送附件
        emailSendDTO.getStuInfoSendList().forEach(stu -> {
            messageSendWithEmailHandler.sendEmail(
                    stu.getEmail(),
                    emailSendDTO.getTittle(),
                    emailSendDTO.getContent(),
                    stu.getStuName(),
                    emailAttachmentList,
                    attachmentInfoList
            );
        });
        log.info("邮箱发送成功!");
    }

    @Override
    public void sendMessageByEmail(Long managerId, EmailMassDTO emailMassDTO, List<MultipartFile> attachmentList) {
        // 查询学生列表
        List<StuBaseInfoDTO> stuBaseInfoDTOS = queryStuList(emailMassDTO.getBatchId(), emailMassDTO.getUserIds());
        // 转换对象
        EmailSendDTO emailSendDTO = new EmailSendDTO();
        emailSendDTO.setTittle(emailMassDTO.getTittle());
        emailSendDTO.setContent(emailMassDTO.getContent());
        emailSendDTO.setStuInfoSendList(stuBaseInfoDTOS);
        // 发送
        sendMessageByEmail(managerId, emailSendDTO, attachmentList);
    }

    private List<AttachmentFile> getAttachUrlList(Long managerId, List<MultipartFile> attachmentList) {
        if(CollectionUtils.isEmpty(attachmentList)) {
            return new ArrayList<>();
        }
        return attachmentList.stream().map(attach -> {
            AttachmentFile attachmentFile = new AttachmentFile();
            // 上传附件
            Long code = resourceService.upload(managerId, attach, AttachmentConstants.ATTACHMENT_ACCESS_LEVEL);
            // 获取系统 url
            String systemUrl = resourceService.getSystemUrl(code);
            attachmentFile.setAttachmentUrl(systemUrl);
            attachmentFile.setFileName(attach.getOriginalFilename());
            return attachmentFile;
        }).toList();
    }
}




