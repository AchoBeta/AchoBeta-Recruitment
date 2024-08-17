package com.achobeta.domain.interview.machine.util;

import cn.hutool.core.collection.CollectionUtil;
import com.achobeta.common.enums.InterviewStateEvent;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.machine.events.external.InterviewStateExternalTransitionHelper;
import com.achobeta.domain.interview.machine.events.internal.InterviewStateInternalTransitionHelper;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.builder.From;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;

import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 1:37
 */
public class InterviewStateMachineUtil {

    private static From<InterviewStatusEnum, InterviewStateEvent, InterviewContext> from(StateMachineBuilder<InterviewStatusEnum, InterviewStateEvent, InterviewContext> builder,
                                                                                         InterviewStatusEnum[] fromState) {
        return Objects.nonNull(fromState) & fromState.length == 1 ?
                builder.externalTransition().from(fromState[0]) : builder.externalTransitions().fromAmong(fromState);
    }

    private static void builderAssign(StateMachineBuilder<InterviewStatusEnum, InterviewStateEvent, InterviewContext> builder,
                                     InterviewStateExternalTransitionHelper helper) {
        InterviewStatusEnum[] fromState = helper.getFromState();
        from(builder, fromState)
                .to(helper.getToState())
                .on(helper.getOnEvent())
                .when(helper.getWhenCondition())
                .perform(helper.getPerformAction());
    }

    private static void builderAssign(StateMachineBuilder<InterviewStatusEnum, InterviewStateEvent, InterviewContext> builder,
                                      InterviewStateInternalTransitionHelper helper) {
        List<InterviewStatusEnum> withinList = helper.getWithinList();
        if(!CollectionUtil.isEmpty(withinList)) {
            InterviewStateEvent onEvent = helper.getOnEvent();
            Condition<InterviewContext> whenCondition = helper.getWhenCondition();
            Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> performAction = helper.getPerformAction();
            withinList.forEach(within -> {
                builder.internalTransition()
                        .within(within)
                        .on(onEvent)
                        .when(whenCondition)
                        .perform(performAction);
            });
        }
    }

    public static void buildMachine(String machineId,
                                    List<InterviewStateExternalTransitionHelper> externalHelpers,
                                    List<InterviewStateInternalTransitionHelper> internalHelpers) {
        StateMachineBuilder<InterviewStatusEnum, InterviewStateEvent, InterviewContext>
                builder = StateMachineBuilderFactory.create();
        // from + event 确定一个流转，状态机以创建的其中一个 from + event 的流转为准
        externalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        // 内部流转无非就是 from == to 的流转罢了，内部流转和外部流转只是状态机的概念，在 fire 状态机的时候用 from + event 确定轮转是哪一个
        internalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        builder.build(machineId);
    }

}
