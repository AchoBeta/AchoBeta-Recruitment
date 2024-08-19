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
/**
 * State：状态
 * Event：事件，状态由事件触发，引起变化
 * Transition：流转，表示从一个状态到另一个状态
 * External Transition：外部流转，两个不同状态之间的流转
 * Internal Transition：内部流转，同一个状态之间的流转
 * Condition：条件，表示是否允许到达某个状态
 * Action：动作，到达某个状态之后，可以做什么
 * StateMachine：状态机
 */
@Configuration
@RequiredArgsConstructor
public class InterviewStateMachineBuildConfig {

    private final List<InterviewStateExternalTransitionHelper> externalHelpers;

    private final List<InterviewStateInternalTransitionHelper> internalHelpers;

    @PostConstruct
    public void buildInterviewMachine() {
        // 这里元素为范型接口的 List 不能直接赋值
        StateMachineUtil.buildMachine(
                InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID,
                externalHelpers,
                internalHelpers
        );
        StateMachineUtil.showMachine(InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID);
    }

}
