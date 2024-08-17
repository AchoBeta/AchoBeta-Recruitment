package com.achobeta.machine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;

import java.util.List;

public interface StateInternalTransitionHelper<S, E, C> {

    List<S> getWithinList();

    E getOnEvent();

    Condition<C> getWhenCondition();

    Action<S, E, C> getPerformAction();
}
