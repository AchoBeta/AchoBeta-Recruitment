package com.achobeta.domain.student.model.converter;




import com.achobeta.domain.message.model.dto.StuOfMessageVO;
import com.achobeta.domain.student.model.dto.StuAttachmentDTO;
import com.achobeta.domain.student.model.dto.StuSimpleResumeDTO;
import com.achobeta.domain.student.model.entity.StuAttachment;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuAttachmentVO;
import com.achobeta.domain.student.model.vo.StuSimpleResumeVO;
import org.mapstruct.Mapper;

import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")//交给spring管理
public interface StuResumeConverter {
    StuResumeConverter STU_RESUME_CONVERTER=Mappers.getMapper(StuResumeConverter.class);

    StuSimpleResumeVO stuResumeToSimpleVO(StuResume stuResume);

    StuAttachmentVO stuAttachmentToVO(StuAttachment stuAttachment);

    List<StuAttachmentVO> stuAttachmentsToVOList(List<StuAttachment> stuAttachmentList);

    StuResume updatePoWithStuSimpleResumeDTO(StuSimpleResumeDTO stuSimpleResumeDTO,@MappingTarget StuResume stuResume);

    StuAttachment stuAttachmentDTOToPo(StuAttachmentDTO stuAttachmentDTO);

    List<StuOfMessageVO> stuResumeListToStuMessageVOList(List<StuResume> stuResumeList);
}
