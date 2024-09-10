package com.achobeta.domain.resumestate.machine.config;

import com.achobeta.domain.resumestate.machine.constants.ResumeStateMachineConstants;
import com.achobeta.domain.resumestate.machine.events.external.ResumeStateExternalTransitionHelper;
import com.achobeta.domain.resumestate.machine.events.internal.ResumeStateInternalTransitionHelper;
import com.achobeta.machine.StateMachineUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 1:54
 */
@Configuration
@RequiredArgsConstructor
public class ResumeStateMachineBuildConfig {

    private final List<ResumeStateExternalTransitionHelper> externalHelpers;

    private final List<ResumeStateInternalTransitionHelper> internalHelpers;

    @PostConstruct
    public void buildInterviewMachine() {
        StateMachineUtil.buildMachine(
                ResumeStateMachineConstants.RESUME_STATE_MACHINE_ID,
                externalHelpers,
                internalHelpers
        );
        StateMachineUtil.showMachine(ResumeStateMachineConstants.RESUME_STATE_MACHINE_ID);
    }

}
