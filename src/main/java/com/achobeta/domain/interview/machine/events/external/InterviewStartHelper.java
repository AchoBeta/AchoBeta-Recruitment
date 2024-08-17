package com.achobeta.domain.interview.machine.events.external;

import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 1:26
 */
@Component
@RequiredArgsConstructor
public class InterviewStartHelper implements InterviewStateExternalTransitionHelper {

    private final Condition<InterviewContext> defaultCondition;

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultAction;

    @Override
    public InterviewStatus[] getFromState() {
        return new InterviewStatus[]{InterviewStatus.NOT_STARTED};
    }

    @Override
    public InterviewStatus getToState() {
        return InterviewStatus.STARTING;
    }

    @Override
    public InterviewEvent getOnEvent() {
        return InterviewEvent.INTERVIEW_START;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return defaultCondition;
    }

    @Override
    public Action<InterviewStatus, InterviewEvent, InterviewContext> getPerformAction() {
        return defaultAction;
    }
}