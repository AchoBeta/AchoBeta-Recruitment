package com.achobeta.domain.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.service.MessageService;
import com.achobeta.domain.message.model.dao.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 马拉圈
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-05-11 02:33:33
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




