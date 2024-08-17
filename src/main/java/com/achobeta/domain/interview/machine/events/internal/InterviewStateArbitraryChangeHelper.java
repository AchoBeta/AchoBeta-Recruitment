package com.achobeta.domain.interview.machine.events.internal;

import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-17
 * Time: 21:02
 */
@Component
@RequiredArgsConstructor
public class InterviewStateArbitraryChangeHelper implements InterviewStateInternalTransitionHelper{

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction;

    @Override
    public List<InterviewStatus> getWithinList() {
        return List.of(InterviewStatus.values());
    }

    @Override
    public InterviewEvent getOnEvent() {
        return InterviewEvent.INTERVIEW_ARBITRARY_CHANGE;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return interviewContext -> Objects.nonNull(interviewContext.getToState());
    }

    @Override
    public Action<InterviewStatus, InterviewEvent, InterviewContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultInterviewAction.execute(from, context.getToState(), event, context);
        };
    }
}
