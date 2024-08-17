package com.achobeta.domain.interview.machine.events.internal;

import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 13:00
 */
public interface InterviewStateInternalTransitionHelper {

    List<InterviewStatus> getWithinList();

    InterviewEvent getOnEvent();

    Condition<InterviewContext> getWhenCondition();

    Action<InterviewStatus, InterviewEvent, InterviewContext> getPerformAction();
}