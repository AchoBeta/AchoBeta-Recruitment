package com.achobeta.machine;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachineFactory;
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
 * Time: 4:01
 */
public class StateMachineUtil {

    public static <S, E, C> StateExternalTransitionHelper<S, E, C> covert(StateExternalTransitionHelper<S, E, C> helper) {
        return helper;
    }

    public static <S, E, C> StateInternalTransitionHelper<S, E, C> covert(StateInternalTransitionHelper<S, E, C> helper) {
        return helper;
    }

    private static <S, E, C> From<S, E, C> from(StateMachineBuilder<S, E, C> builder, S[] fromState) {
        return Objects.nonNull(fromState) & fromState.length == 1 ?
                builder.externalTransition().from(fromState[0]) : builder.externalTransitions().fromAmong(fromState);
    }

    private static <S, E, C> void builderAssign(StateMachineBuilder<S, E, C> builder,
                                                StateExternalTransitionHelper<S, E, C> helper) {
        S[] fromState = helper.getFromState();
        from(builder, fromState)
                .to(helper.getToState())
                .on(helper.getOnEvent())
                .when(helper.getWhenCondition())
                .perform(helper.getPerformAction());
    }

    private static <S, E, C> void builderAssign(StateMachineBuilder<S, E, C> builder,
                                                StateInternalTransitionHelper<S, E, C> helper) {
        List<S> withinList = helper.getWithinList();
        if(!CollectionUtil.isEmpty(withinList)) {
            E onEvent = helper.getOnEvent();
            Condition<C> whenCondition = helper.getWhenCondition();
            Action<S, E, C> performAction = helper.getPerformAction();
            withinList.forEach(within -> {
                builder.internalTransition()
                        .within(within)
                        .on(onEvent)
                        .when(whenCondition)
                        .perform(performAction);
            });
        }
    }

    public static <S, E, C> void buildMachine(String machineId,
                                              List<StateExternalTransitionHelper<S, E, C>> externalHelpers,
                                              List<StateInternalTransitionHelper<S, E, C>> internalHelpers) {
        StateMachineBuilder<S, E, C> builder = StateMachineBuilderFactory.create();
        externalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        internalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        builder.build(machineId);
    }

    public static void showMachine(String machineId) {
        StateMachineFactory.get(machineId).showStateMachine();
    }

    public static <S, E, C> S fireEvent(String machineId, S state, E event, C context) {
        return (S) StateMachineFactory.get(machineId).fireEvent(state, event, context);
    }
}
