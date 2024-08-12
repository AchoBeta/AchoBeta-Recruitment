package com.achobeta.domain.interview.machine.constants;

import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Condition;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:37
 */
public interface InterviewStateMachineConstants {

    String INTERVIEW_STATE_MACHINE_ID = "interviewStateMachineId";

    static Condition<InterviewContext> DEFAULT_CONDITION() {
        return interviewContext -> Boolean.TRUE;
    }


}
