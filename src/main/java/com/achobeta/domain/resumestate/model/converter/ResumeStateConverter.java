package com.achobeta.domain.resumestate.model.converter;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.model.entity.ResumeStatusProcess;
import com.achobeta.domain.resumestate.model.vo.ResumeEventVO;
import com.achobeta.domain.resumestate.model.vo.ResumeStatusProcessVO;
import com.achobeta.domain.resumestate.model.vo.ResumeStatusVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 4:09
 */
@Mapper
public interface ResumeStateConverter {

    ResumeStateConverter INSTANCE = Mappers.getMapper(ResumeStateConverter.class);

    List<ResumeStatusVO> resumeStatusListToResumeStatusVOList(List<ResumeStatus> resumeStatusList);

    List<ResumeEventVO> resumeEventListToResumeEventVOList(List<ResumeEvent> resumeEventList);

    List<ResumeStatusProcessVO> processesToProcessVOList(List<ResumeStatusProcess> resumeStatusProcesses);

}
