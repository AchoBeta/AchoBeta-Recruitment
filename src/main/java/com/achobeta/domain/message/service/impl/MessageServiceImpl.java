package com.achobeta.domain.message.service.impl;

import com.achobeta.common.base.BasePageResultEntity;
import com.achobeta.domain.message.converter.MessageConverter;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.dto.StuOfMessageVO;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.student.model.converter.StuResumeConverter;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;

import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.redis.RedisCache;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.domain.message.model.dao.mapper.MessageMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author cattleyuan
 * @description 针对表【message(“活动参与”表)】的数据库操作Service实现
 * @createDate 2024-08-11 22:20:17
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    private final StuResumeService stuResumeService;
    private final StuResumeConverter stuResumeConverter;
    private final MessageConverter messageConverter;
    private final RedisCache redisCache;

    @Override
    public BasePageResultEntity<StuOfMessageVO> queryStuListByCondition(QueryStuListDTO queryStuDTO) {


        //查询分页返回结果
        Page<StuResume> pageResult = getStuResumePage(queryStuDTO);

        //封装返回结果
        BasePageResultEntity<StuOfMessageVO> PageResultEntity = getStuOfMessagePageResultEntity(pageResult);

        return PageResultEntity;
    }

    @Override
    public void sendMessage(MessageContentDTO messageContentBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {

        MessageContentVO messageContentVO = messageConverter.messageContentDTOToVO(messageContentBody);
        webSocketSet.stream()
                .filter(server -> !messageContentBody.getUserIdList().contains(server.getUserId()))
                .forEach(messageReceiveServer -> {
                    //消息内容
                    String messageText = JSON.toJSONString(messageContentVO);
                    //消息发送
                    try {
                        messageReceiveServer.getSession().getBasicRemote().sendText(messageText);
                    } catch (IOException e) {
                        String message = String.format("用户id->%d，消息发送失败!", messageReceiveServer.getUserId());
                        log.error(message);
                    }
                });
    }

    @Override
    public void storeMessage(MessageContentDTO messageContent) {
        //获取当前管理员id
        long managerId = BaseContext.getCurrentUser().getUserId();
        //保存消息到数据库
        Message message=messageConverter.messsageContentDTOToPo(messageContent);
        this.save(message);

        //保存消息到缓存
        messageContent.getUserIdList()
                .stream()
                .forEach(receiverId ->
                        redisCache.setCacheObject(MESSAGE_CACHE_NAME + receiverId + managerId, message));

    }

    @Override
    public List<MessageContentVO> getMessageListofUser() {
        long userId = BaseContext.getCurrentUser().getUserId();
        Optional<List<MessageContentVO>> messageOptional = redisCache.getCacheList(MESSAGE_CACHE_NAME + userId + "*", MessageContentVO.class);

        if(!messageOptional.isEmpty())
            return messageOptional.get();

        return Collections.emptyList();
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
                .like(!queryStuDTO.getName().isBlank(), StuResume::getName, queryStuDTO.getName())
                .eq(queryStuDTO.getBatchId() != null, StuResume::getBatchId, queryStuDTO.getBatchId())
                .eq(queryStuDTO.getGrade() != null, StuResume::getGrade, queryStuDTO.getGrade())
                .eq(queryStuDTO.getStatus() != null, StuResume::getStatus, queryStuDTO.getStatus())
                .page(page);
        return pageResult;
    }
}




