package com.achobeta.domain.interview.machine.events.external;

import com.achobeta.common.enums.InterviewStateEvent;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 1:10
 */
public interface InterviewStateExternalTransitionHelper {

    InterviewStatusEnum[] getFromState();

    InterviewStatusEnum getToState();

    InterviewStateEvent getOnEvent();

    Condition<InterviewContext> getWhenCondition();

    Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> getPerformAction();

}
