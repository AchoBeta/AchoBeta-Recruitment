package com.achobeta.domain.recruit.model.condition.anymatch;

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
 * Time: 10:18
 */
public class UserIdCondition extends ArrayList<Long> implements Condition {

    @Override
    public Predicate<StuResume> predicate() {
        return stuResume -> {
            return this.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(userId -> userId.equals(stuResume.getUserId()));
        };
    }
}