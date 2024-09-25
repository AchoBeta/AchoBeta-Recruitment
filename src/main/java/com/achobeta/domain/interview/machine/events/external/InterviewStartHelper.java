package com.achobeta.domain.interview.machine.events.external;

import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private final Condition<InterviewContext> defaultInterviewCondition;

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction;

    @Override
    public List<InterviewStatus> getFromState() {
        return List.of(
                InterviewStatus.NOT_STARTED
        );
    }

    @Override
    public InterviewStatus getToState(InterviewStatus from) {
        return InterviewStatus.STARTING;
    }

    @Override
    public InterviewEvent getOnEvent() {
        return InterviewEvent.INTERVIEW_START;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return defaultInterviewCondition;
    }

    @Override
    public Action<InterviewStatus, InterviewEvent, InterviewContext> getPerformAction() {
        return defaultInterviewAction;
    }
}
