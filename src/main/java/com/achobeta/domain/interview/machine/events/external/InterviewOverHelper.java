package com.achobeta.domain.interview.machine.events.external;

import com.achobeta.common.enums.InterviewStateEvent;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.interview.machine.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 3:19
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InterviewOverHelper implements InterviewStateExternalTransitionHelper{

    private final Condition<InterviewContext> defaultCondition;

    private final Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> defaultAction;

    @Override
    public InterviewStatusEnum[] getFromState() {
        return new InterviewStatusEnum[]{InterviewStatusEnum.STARTING, InterviewStatusEnum.NOT_STARTED};
    }

    @Override
    public InterviewStatusEnum getToState() {
        return InterviewStatusEnum.ENDED;
    }

    @Override
    public InterviewStateEvent getOnEvent() {
        return InterviewStateEvent.INTERVIEW_OVER;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return defaultCondition;
    }

    @Override
    public Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> getPerformAction() {
        return defaultAction;
    }
}
