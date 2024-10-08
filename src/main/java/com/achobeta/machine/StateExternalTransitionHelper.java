package com.achobeta.machine;

import com.achobeta.exception.GlobalServiceException;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 2:12
 */
public interface StateExternalTransitionHelper<S, E, C> {

    List<S> getFromState();

    S getToState(S from) throws GlobalServiceException;

    E getOnEvent();

    Condition<C> getWhenCondition();

    // 不应该在此方法上加 @Transactional，因为这个方法是 bean 的自调用，事务并不会生效，除非调用者存在事务，同理，AOP 也是
    Action<S, E, C> getPerformAction();

}
