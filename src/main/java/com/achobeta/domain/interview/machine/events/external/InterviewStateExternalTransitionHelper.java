package com.achobeta.domain.interview.machine.events.external;

import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.machine.StateExternalTransitionHelper;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 1:10
 */
public interface InterviewStateExternalTransitionHelper
        extends StateExternalTransitionHelper<InterviewStatus, InterviewEvent, InterviewContext> {

}
