package com.achobeta.domain.recruit.model.json;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.domain.student.model.entity.StuResume;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Objects;
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
public class StudentGroup {

    @Valid
    private AnyMatchCondition anyMatch; // null 代表不限制此条件，若非 null，则学生必须满足 anyMatch 内至少一个条件

    @Valid
    private AllMatchCondition allMatch; // null 代表不限制此条件，若非 null，则学生必须满足 allMatch 内的所有条件

    public Predicate<StuResume> predicate() {
        Predicate<StuResume> predicate = stuResume -> Boolean.TRUE;
        return predicate.and(stuResume -> Objects.isNull(anyMatch) || anyMatch.predicate().test(stuResume))
                .and(stuResume -> Objects.isNull(allMatch) || allMatch.predicate().test(stuResume));
    }

}
