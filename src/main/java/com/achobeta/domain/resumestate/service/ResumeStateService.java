package com.achobeta.domain.resumestate.service;

import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.model.vo.ResumeStatusProcessVO;
import com.achobeta.domain.student.model.entity.StuResume;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 0:13
 */
public interface ResumeStateService {

    void switchResumeState(Long resumeId, ResumeStatus status, ResumeEvent event);

    ResumeStatus executeResumeEvent(ResumeEvent resumeEvent, ResumeContext resumeContext);

    List<ResumeStatusProcessVO> getProcessByResumeId(StuResume currentResume);

}
