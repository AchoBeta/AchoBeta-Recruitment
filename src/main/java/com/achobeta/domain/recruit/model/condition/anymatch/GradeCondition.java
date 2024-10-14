package com.achobeta.domain.recruit.model.condition.anymatch;

import com.achobeta.domain.recruit.model.condition.function.StudentCondition;
import com.achobeta.domain.student.model.entity.StuResume;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 10:17
 */
public class GradeCondition extends ArrayList<Integer> implements StudentCondition {

    @Override
    public Predicate<StuResume> predicate() {
        return stuResume -> this.stream()
                .filter(Objects::nonNull)
                .anyMatch(grade -> grade.equals(stuResume.getGrade()));
    }
}
