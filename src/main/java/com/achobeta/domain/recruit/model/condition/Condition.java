package com.achobeta.domain.recruit.model.condition;

import com.achobeta.domain.student.model.entity.StuResume;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 10:12
 */
public interface Condition {

    default Predicate<StuResume> skipAndIfNull(Condition condition) {
        Predicate<StuResume> predicate = t -> Boolean.TRUE;
        return predicate.and(stuResume -> Objects.isNull(condition) || condition.predicate().test(stuResume));
    }

    default Predicate<StuResume> skipOrIfNull(Condition condition) {
        Predicate<StuResume> predicate = t -> Boolean.FALSE;
        return predicate.and(stuResume -> Objects.nonNull(condition) && condition.predicate().test(stuResume));
    }

    Predicate<StuResume> predicate();
}
