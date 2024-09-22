package com.achobeta.machine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;

import java.util.List;

public interface StateInternalTransitionHelper<S, E, C> {

    List<S> getWithin();

    E getOnEvent();

    Condition<C> getWhenCondition();

    // 不应该在此方法上加 @Transactional，因为这个方法是 bean 的自调用，事务并不会生效，除非调用者存在事务
    Action<S, E, C> getPerformAction();
}
