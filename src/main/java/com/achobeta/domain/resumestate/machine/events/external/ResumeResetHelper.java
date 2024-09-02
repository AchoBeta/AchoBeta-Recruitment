package com.achobeta.domain.resumestate.machine.events.external;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
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
 * Time: 3:49
 */
@Component
@RequiredArgsConstructor
public class ResumeResetHelper implements ResumeStateExternalTransitionHelper{

    private final Condition<ResumeContext> defaultResumeCondition;

    private final Action<ResumeStatus, ResumeEvent, ResumeContext> defaultResumeAction;

    private final StuResumeService stuResumeService;

    @Override
    public List<ResumeStatus> getFromState() {
        return List.of(ResumeStatus.values());
    }

    @Override
    public ResumeStatus getToState(ResumeStatus from) {
        return ResumeStatus.DRAFT;
    }

    @Override
    public ResumeEvent getOnEvent() {
        return ResumeEvent.RESET;
    }

    @Override
    public Condition<ResumeContext> getWhenCondition() {
        return defaultResumeCondition;
    }

    @Override
    public Action<ResumeStatus, ResumeEvent, ResumeContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultResumeAction.execute(from, to, event, context);
            // 简历提交次数刷新
            stuResumeService.lambdaUpdate()
                    .eq(StuResume::getId, context.getResume().getId())
                    .set(StuResume::getSubmitCount, 0)
                    .update();
        };
    }
}
