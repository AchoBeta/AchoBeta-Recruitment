package com.achobeta.util;

import com.alibaba.cola.statemachine.StateMachineFactory;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 4:01
 */
public class StateMachineUtil {

    public static void showMachine(String machineId) {
        StateMachineFactory.get(machineId).showStateMachine();
    }

    public static <S, E, C> S fireEvent(String machineId, S state, E event, C context) {
        return (S) StateMachineFactory.get(machineId).fireEvent(state, event, context);
    }
}
