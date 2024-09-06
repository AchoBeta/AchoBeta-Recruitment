package com.achobeta.domain.recruit.model.condition;

import com.achobeta.domain.student.model.entity.StuResume;

import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 10:12
 */
public interface Condition {

    Predicate<StuResume> predicate();
}
