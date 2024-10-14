package com.achobeta.domain.message.model.converter;


import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.message.model.dto.MessageContentDTO;
import com.achobeta.domain.message.model.dto.QueryStuListDTO;
import com.achobeta.domain.message.model.dto.StuBaseInfoDTO;
import com.achobeta.domain.message.model.entity.Message;
import com.achobeta.domain.message.model.vo.MessageContentVO;
import com.achobeta.domain.message.model.vo.QueryStuListVO;
import com.achobeta.domain.student.model.entity.StuResume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface MessageConverter {
    MessageConverter MESSAGE_CONVERTER = Mappers.getMapper(MessageConverter.class);


    MessageContentVO messageContentDTOToVO(MessageContentDTO messageContentBody);

    Message messsageContentDTOToPo(MessageContentDTO messageContent);

    List<MessageContentVO> messageContentPoToVOList(List<Message> messageList);

    BasePageQuery queryStuListDTOToBasePageQuery(QueryStuListDTO queryStuListDTO);

    QueryStuListVO basePageResultToQueryStuListVO(BasePageResult<StuResume> basePageResult);

    @Mapping(target = "stuName", source = "name")
    StuBaseInfoDTO stuResumeToStuBaseInfoDTO(StuResume stuResume);

    List<StuBaseInfoDTO> stuResumeListToStuBaseInfoDTOList(List<StuResume> stuResumeList);
}
