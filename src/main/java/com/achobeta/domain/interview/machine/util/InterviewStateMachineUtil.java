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

    private static void builderAssign(StateMachineBuilder<InterviewStatusEnum, InterviewStateEvent, InterviewContext> builder,
                                     InterviewStateExternalTransitionHelper helper) {
        InterviewStatusEnum[] fromState = helper.getFromState();
        From<InterviewStatusEnum, InterviewStateEvent, InterviewContext> from = null;
        if(Objects.nonNull(fromState) & fromState.length == 1) {
            from = builder.externalTransition().from(fromState[0]);
        } else {
            from = builder.externalTransitions().fromAmong(fromState);
        }
        from.to(helper.getToState())
                .on(helper.getOnEvent())
                .when(helper.getWhenCondition())
                .perform(helper.getPerformAction());
    }

    private static void builderAssign(StateMachineBuilder<InterviewStatusEnum, InterviewStateEvent, InterviewContext> builder,
                                      InterviewStateInternalTransitionHelper helper) {
        List<InterviewStatusEnum> withinList = helper.getWithinList();
        InterviewStateEvent onEvent = helper.getOnEvent();
        Condition<InterviewContext> whenCondition = helper.getWhenCondition();
        Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> performAction = helper.getPerformAction();
        if(!CollectionUtil.isEmpty(withinList)) {
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
        externalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        internalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        builder.build(machineId);
    }

}
