package com.achobeta.domain.interview.machine.events.internal;

import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.machine.StateInternalTransitionHelper;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 13:00
 */
public interface InterviewStateInternalTransitionHelper
        extends StateInternalTransitionHelper<InterviewStatus, InterviewEvent, InterviewContext> {

}
