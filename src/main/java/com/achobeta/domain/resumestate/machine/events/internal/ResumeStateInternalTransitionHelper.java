package com.achobeta.domain.resumestate.machine.events.internal;

import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.machine.StateInternalTransitionHelper;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 13:00
 */
public interface ResumeStateInternalTransitionHelper extends StateInternalTransitionHelper<ResumeStatus, ResumeEvent, ResumeContext> {

}
