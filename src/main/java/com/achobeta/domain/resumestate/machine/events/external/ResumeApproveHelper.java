package com.achobeta.domain.resumestate.machine.events.external;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.constants.ResumeStateMachineConstants;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.machine.StateMachineUtil;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.achobeta.common.enums.ResumeStatus.*;

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

    /**
     * 默认尝试转正：
     * <br />
     * 若请求没有携带“创建正式成员相关信息”，转正必然不会成功，通过这一事件也不受影响
     * <br />
     * 若是携带了，转正执行期间可能会抛异常，从而导致通过这一事件失败
     */
    @Override
    public Action<ResumeStatus, ResumeEvent, ResumeContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultResumeAction.execute(from, to, event, context);
            StateMachineUtil.fireEvent(
                    ResumeStateMachineConstants.RESUME_STATE_MACHINE_ID,
                    to,
                    ResumeEvent.CONFIRM,
                    context
            );
        };
    }
}