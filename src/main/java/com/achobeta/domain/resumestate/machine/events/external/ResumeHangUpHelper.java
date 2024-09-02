package com.achobeta.domain.resumestate.machine.events.external;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 3:19
 */
@Component
@RequiredArgsConstructor
public class ResumeHangUpHelper implements ResumeStateExternalTransitionHelper{

    private final Condition<ResumeContext> defaultResumeCondition;

    private final Action<ResumeStatus, ResumeEvent, ResumeContext> defaultResumeAction;

    @Override
    public List<ResumeStatus> getFromState() {
        return List.of(ResumeStatus.values());
    }

    @Override
    public ResumeStatus getToState(ResumeStatus from) {
        return ResumeStatus.SUSPENDED;
    }

    @Override
    public ResumeEvent getOnEvent() {
        return ResumeEvent.SUSPEND;
    }

    @Override
    public Condition<ResumeContext> getWhenCondition() {
        return defaultResumeCondition;
    }

    @Override
    public Action<ResumeStatus, ResumeEvent, ResumeContext> getPerformAction() {
        return defaultResumeAction;
    }
}
