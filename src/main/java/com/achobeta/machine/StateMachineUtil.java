package com.achobeta.machine;

import cn.hutool.core.collection.CollectionUtil;
import com.achobeta.exception.GlobalServiceException;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 4:01
 */
/**
 * Alibaba Cola 状态机关键词
 * State：状态
 * Event：事件，状态由事件触发，引起变化
 * Transition：流转，表示从一个状态到另一个状态
 * External Transition：外部流转，两个不同状态之间的流转
 * Internal Transition：内部流转，同一个状态之间的流转
 * Condition：条件，表示是否允许到达某个状态
 * Action：动作，到达某个状态之后，可以做什么
 * StateMachine：状态机
 */
@Slf4j
public class StateMachineUtil {

    private static <S, E, C> void builderAssign(StateMachineBuilder<S, E, C> builder,
                                                StateExternalTransitionHelper<S, E, C> helper) {
        List<S> fromStateList = helper.getFromState();
        if(!CollectionUtil.isEmpty(fromStateList)) {
            E onEvent = helper.getOnEvent();
            Condition<C> whenCondition = helper.getWhenCondition();
            Action<S, E, C> performAction = helper.getPerformAction();
            fromStateList.forEach(fromState -> {
                try {
                    S toState = helper.getToState(fromState); // 若抛异常就忽略这一个，构造下一个状态轮转
                    // 不保证每个 toState 相等的情况下，不用 externalTransitions 与 fromAmong
                    builder.externalTransition()
                            .from(fromState)
                            .to(toState)
                            .on(onEvent)
                            .when(whenCondition)
                            .perform(performAction);
                } catch (GlobalServiceException e) {
                    log.warn(e.getMessage());
                }
            });
        }
    }

    private static <S, E, C> void builderAssign(StateMachineBuilder<S, E, C> builder,
                                                StateInternalTransitionHelper<S, E, C> helper) {
        List<S> withinList = helper.getWithin();
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
                                              List<? extends StateExternalTransitionHelper<S, E, C>> externalHelpers,
                                              List<? extends StateInternalTransitionHelper<S, E, C>> internalHelpers) {
        // 创建一个 builder
        StateMachineBuilder<S, E, C> builder = StateMachineBuilderFactory.create();
        // 添加轮转
        externalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        internalHelpers.forEach(helper -> {
            builderAssign(builder, helper);
        });
        // 创建状态机
        builder.build(machineId);
    }

    public static <S, E, C> StateMachine<S, E, C> getMachine(String machineId) {
        return StateMachineFactory.get(machineId);
    }

    public static void showMachine(String machineId) {
        getMachine(machineId).showStateMachine();
    }

    public static String generatePlantUML(String machineId) {
        return getMachine(machineId).generatePlantUML();
    }

    public static <S, E, C> void printMachine(String machineId) {
        StateMachine<S, E, C> stateMachine = getMachine(machineId);
        stateMachine.showStateMachine();
        System.out.println(stateMachine.generatePlantUML());
    }

    public static <S, E, C> S fireEvent(String machineId, S state, E event, C context) {
        return (S) getMachine(machineId).fireEvent(state, event, context);
    }
}
