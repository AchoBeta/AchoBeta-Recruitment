package com.achobeta.domain.resumestate.service;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 0:13
 */
public interface ResumeStateService {

    void switchResumeState(Long resumeId, ResumeStatus status);

    ResumeStatus executeResumeEvent(ResumeEvent resumeEvent, ResumeContext resumeContext);

}
