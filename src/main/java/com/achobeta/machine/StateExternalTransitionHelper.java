package com.achobeta.machine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 2:12
 */
public interface StateExternalTransitionHelper<S, E, C> {

    S[] getFromState();

    S getToState();

    E getOnEvent();

    Condition<C> getWhenCondition();

    Action<S, E, C> getPerformAction();

}
