package com.achobeta.domain.resumestate.machine.events.external;

import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.machine.StateExternalTransitionHelper;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 1:10
 */
public interface ResumeStateExternalTransitionHelper extends StateExternalTransitionHelper<ResumeStatus, ResumeEvent, ResumeContext> {

}
