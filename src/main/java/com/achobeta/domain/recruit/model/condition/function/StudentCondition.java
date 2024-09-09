package com.achobeta.domain.recruit.model.condition.function;

import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.util.ObjectUtil;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 10:12
 */
public interface StudentCondition {

    Predicate<StuResume> predicate();

    static <C extends StudentCondition> Predicate<StuResume> predicate(C object, Predicate<StuResume> identity, BinaryOperator<Predicate<StuResume>> accumulator) {
        return ObjectUtil.reduce(
                object,
                StudentCondition.class,
                StudentCondition::predicate,
                identity,
                accumulator
        );
    }

    default Predicate<StuResume> predicate(Predicate<StuResume> identity, BinaryOperator<Predicate<StuResume>> accumulator) {
        return predicate(this, identity, accumulator);
    }

    default Predicate<StuResume> anyMatch() {
        return predicate(stuResume -> Boolean.FALSE, Predicate::or);
    }

    default Predicate<StuResume> allMatch() {
        return predicate(stuResume -> Boolean.TRUE, Predicate::and);
    }
}
