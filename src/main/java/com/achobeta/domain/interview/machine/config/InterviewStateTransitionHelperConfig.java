package com.achobeta.domain.interview.machine.config;

import com.achobeta.common.enums.InterviewStateEvent;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.interview.machine.InterviewContext;
import com.achobeta.domain.interview.machine.constants.InterviewStateMachineConstants;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.util.StateMachineUtil;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class InterviewStateTransitionHelperConfig {

    private final InterviewService interviewService;

    @Bean
    public Condition<InterviewContext> defaultCondition() {
        return interviewContext -> Boolean.TRUE;
    }

    @Bean
    public Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> defaultAction() {
        return (from, to, event, context) -> {
            Interview currentInterview = context.getInterview();
            log.info("state from {} to {} run {} currentInterview {} managerId {}",
                    from, to, event, currentInterview.getId(), context.getManagerId());
            // 修改面试状态
            interviewService.switchInterview(currentInterview.getId(), to);
            currentInterview.setStatus(to);
            // 面试通知
            StateMachineUtil.fireEvent(InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID,
                    to, InterviewStateEvent.INTERVIEW_STARTING_NOTICE, context);
        };
    }

}
