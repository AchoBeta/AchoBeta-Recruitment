package com.achobeta.domain.resumestate.machine.events.external;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.exception.GlobalServiceException;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.achobeta.domain.resumestate.enums.ResumeStatus.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 3:28
 */
@Component
@RequiredArgsConstructor
public class ResumeApproveHelper implements ResumeStateExternalTransitionHelper {

    private final Condition<ResumeContext> defaultResumeCondition;

    private final Action<ResumeStatus, ResumeEvent, ResumeContext> defaultResumeAction;

    private final Action<ResumeStatus, ResumeEvent, ResumeContext> resumeNotice;

    @Override
    public List<ResumeStatus> getFromState() {
        return List.of(
                PENDING_INITIAL_INTERVIEW,
                PENDING_SECOND_INTERVIEW,
                PENDING_FINAL_INTERVIEW
        );
    }

    @Override
    public ResumeStatus getToState(ResumeStatus from) throws GlobalServiceException {
        return switch (from) {
            case PENDING_INITIAL_INTERVIEW -> INITIAL_INTERVIEW_PASSED;
            case PENDING_SECOND_INTERVIEW -> SECOND_INTERVIEW_PASSED;
            case PENDING_FINAL_INTERVIEW -> FINAL_INTERVIEW_PASSED;
            default -> throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_EXCEPTION);
        };
    }

    @Override
    public ResumeEvent getOnEvent() {
        return ResumeEvent.APPROVE;
    }

    @Override
    public Condition<ResumeContext> getWhenCondition() {
        return defaultResumeCondition;
    }

    @Override
    public Action<ResumeStatus, ResumeEvent, ResumeContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultResumeAction.execute(from, to, event, context);
            // 判断是否同时发送简历状态变更的邮件
            resumeNotice.execute(from, to, event, context);
        };
    }
}
