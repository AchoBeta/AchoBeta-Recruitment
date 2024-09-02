package com.achobeta.domain.resumestate.machine.config;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 1:55
 */
@Configuration
@RequiredArgsConstructor
public class ResumeStateTransitionHelperConfig {

    @Bean
    public Condition<ResumeContext> defaultResumeCondition() {
        return resumeContext -> Boolean.TRUE;
    }

    @Bean
    public Action<ResumeStatus, ResumeEvent, ResumeContext> defaultResumeAction() {
        return (from, to, event, context) -> {
            context.log(from, to, event);
            context.getResume().setStatus(to);
        };
    }
}
