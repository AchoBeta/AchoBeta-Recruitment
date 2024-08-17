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
 * Time: 3:21
 */
@Component
@RequiredArgsConstructor
public class InterviewStartAgainHelper implements InterviewStateExternalTransitionHelper {

    private final Condition<InterviewContext> defaultInterviewCondition;

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction;

    @Override
    public InterviewStatus[] getFromState() {
        return new InterviewStatus[]{InterviewStatus.ENDED};
    }

    @Override
    public InterviewStatus getToState() {
        return InterviewStatus.STARTING;
    }

    @Override
    public InterviewEvent getOnEvent() {
        return InterviewEvent.INTERVIEW_START_AGAIN;
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
