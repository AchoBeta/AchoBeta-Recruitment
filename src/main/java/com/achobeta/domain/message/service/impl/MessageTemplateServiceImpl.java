package com.achobeta.domain.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.message.converter.MessageTemplateConverter;
import com.achobeta.domain.message.model.dto.AddMessageTemplateDTO;
import com.achobeta.domain.message.model.dto.UpdateMessageTemplateDTO;
import com.achobeta.domain.message.model.vo.MessageTemplateVO;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.message.model.entity.MessageTemplate;
import com.achobeta.domain.message.service.MessageTemplateService;
import com.achobeta.domain.message.model.dao.mapper.MessageTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author cattleyuan
 * @description 针对表【message_template(模板消息表)】的数据库操作Service实现
 * @createDate 2024-07-10 16:57:24
 */
@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate>
        implements MessageTemplateService {

    private final MessageTemplateConverter messageTemplateConverter;

    @Override
    public void checkMessageTemplateIfExist(Long messageTemplateId) {
        Optional.ofNullable(getById(messageTemplateId)).
                orElseThrow(()-> new GlobalServiceException(GlobalServiceStatusCode.MESSAGE_TEMPLATE_NOT_EXIST));
    }

    @Override
    public void addMessageTemplate(AddMessageTemplateDTO addMessageTemplateDTO) {
        //构建消息模板
        MessageTemplate messageTemplate = messageTemplateConverter.addMessageTemplateDTOToPo(addMessageTemplateDTO);
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
        MessageTemplate messageTemplate=messageTemplateConverter.updateMessageTemplateDTOToPo(updateMessageTemplateDTO);
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
        List<MessageTemplateVO> messageTemplateVOList = messageTemplateConverter.messageTemplatesToVOList(messageTemplateList);

        return messageTemplateVOList;
    }
}




