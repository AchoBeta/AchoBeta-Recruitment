package com.achobeta.domain.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.message.model.dao.mapper.MessageTemplateMapper;
import com.achobeta.domain.message.model.dto.AddMessageTemplateDTO;
import com.achobeta.domain.message.model.dto.UpdateMessageTemplateDTO;
import com.achobeta.domain.message.model.entity.MessageTemplate;
import com.achobeta.domain.message.model.vo.MessageTemplateVO;
import com.achobeta.domain.message.service.MessageTemplateService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cattleyuan
 * @description 针对表【message_template(模板消息表)】的数据库操作Service实现
 * @createDate 2024-07-10 16:57:24
 */
@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate>
        implements MessageTemplateService {

    @Override
    public void addMessageTemplate(AddMessageTemplateDTO addMessageTemplateDTO) {
        //构建消息模板
        MessageTemplate messageTemplate = new MessageTemplate();
        BeanUtil.copyProperties(addMessageTemplateDTO, messageTemplate);
        //保存模板
        boolean isSuccess = save(messageTemplate);
        if (!isSuccess) {
            throw new GlobalServiceException("添加失败",GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL);
        }
    }

    @Override
    public void removeMessageTemplate(Long templateId) {
        boolean isSuccess = removeById(templateId);
        if(!isSuccess){
            throw new GlobalServiceException("删除失败",GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL);
        }
    }

    @Override
    public void updateMessageTemplate(UpdateMessageTemplateDTO updateMessageTemplateDTO) {
        //构建消息模板
        MessageTemplate messageTemplate = new MessageTemplate();
        BeanUtil.copyProperties(updateMessageTemplateDTO,messageTemplate);
        //更新消息模板
        boolean isSuccess = updateById(messageTemplate);
        if(!isSuccess){
            throw new GlobalServiceException("修改失败",GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL);
        }

    }

    @Override
    public List<MessageTemplateVO> queryMessageTemplateList() {
        //查询消息模板列表
        List<MessageTemplate> messageTemplateList = lambdaQuery().list();
        //封装返回结果
        List<MessageTemplateVO> messageTemplateVOList = BeanUtil.copyToList(messageTemplateList, MessageTemplateVO.class);

        return messageTemplateVOList;
    }
}




