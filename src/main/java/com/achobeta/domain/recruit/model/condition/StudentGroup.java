package com.achobeta.domain.recruit.model.condition;

import com.achobeta.domain.recruit.model.condition.function.StudentCondition;
import com.achobeta.domain.student.model.entity.StuResume;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 12:57
 *
 */
@Data
public class StudentGroup implements StudentCondition {

    @Valid
    private StudentAnyMatch anyMatch; // null 代表不限制此条件，若非 null，则学生必须满足 anyMatch 内至少一个条件

    @Valid
    private StudentAllMatch allMatch; // null 代表不限制此条件，若非 null，则学生必须满足 allMatch 内的所有条件

    @Override
    public Predicate<StuResume> predicate() {
        return allMatch();
    }

}
