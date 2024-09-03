package com.achobeta.domain.interview.machine.config;

import com.achobeta.domain.interview.machine.constants.InterviewStateMachineConstants;
import com.achobeta.domain.interview.machine.events.external.InterviewStateExternalTransitionHelper;
import com.achobeta.domain.interview.machine.events.internal.InterviewStateInternalTransitionHelper;
import com.achobeta.machine.StateMachineUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:45
 */
@Configuration
@RequiredArgsConstructor
public class InterviewStateMachineBuildConfig {

    private final List<InterviewStateExternalTransitionHelper> externalHelpers;

    private final List<InterviewStateInternalTransitionHelper> internalHelpers;

    @PostConstruct
    public void buildInterviewMachine() {
        StateMachineUtil.buildMachine(
                InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID,
                externalHelpers,
                internalHelpers
        );
        StateMachineUtil.printMachine(InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID);
    }

}
