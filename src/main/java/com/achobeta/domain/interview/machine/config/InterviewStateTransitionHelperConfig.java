package com.achobeta.domain.interview.machine.config;

import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 17:02
 */
@Configuration
@RequiredArgsConstructor
public class InterviewStateTransitionHelperConfig {

    @Bean
    public Condition<InterviewContext> defaultInterviewCondition() {
        return interviewContext -> Boolean.TRUE;
    }

    @Bean
    public Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction() {
        return (from, to, event, context) -> {
            context.log(from, to, event);
            context.getInterview().setStatus(to);
            context.setHit(Boolean.TRUE);
//            // 面试通知
//            if(!InterviewEvent.INTERVIEW_STARTING_NOTICE.equals(event)) {
//                StateMachineUtil.fireEvent(InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID,
//                        to, InterviewEvent.INTERVIEW_STARTING_NOTICE, context);
//            }
        };
    }

}
