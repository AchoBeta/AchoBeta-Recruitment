package com.achobeta.domain.recruit.model.condition.allmatch;

import com.achobeta.domain.recruit.model.condition.Condition;
import com.achobeta.domain.student.model.entity.StuResume;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 10:11
 */
public class StatusCondition extends ArrayList<Integer> implements Condition {

    @Override
    public Predicate<StuResume> predicate() {
        return stuResume -> {
            return this.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(status -> status.equals(stuResume.getStatus().getCode()));
        };
    }

}