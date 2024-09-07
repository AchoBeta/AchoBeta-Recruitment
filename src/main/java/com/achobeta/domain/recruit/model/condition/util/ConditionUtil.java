package com.achobeta.domain.recruit.model.condition.util;

import com.achobeta.domain.recruit.model.condition.Condition;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.util.ObjectUtil;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 12:34
 */
public class ConditionUtil {

    public static <C extends Condition> Predicate<StuResume> predicate(C object, Predicate<StuResume> identity, BinaryOperator<Predicate<StuResume>> accumulator) {
        return ObjectUtil.reduce(
                object,
                Condition.class,
                Condition::predicate,
                identity,
                accumulator
        );
    }

    public static <C extends Condition> Predicate<StuResume> anyMatch(C object) {
        return predicate(
                object,
                stuResume -> Boolean.FALSE,
                Predicate::or
        );
    }

    public static <C extends Condition> Predicate<StuResume> allMatch(C object) {
        return predicate(
                object,
                stuResume -> Boolean.TRUE,
                Predicate::and
        );
    }

}
