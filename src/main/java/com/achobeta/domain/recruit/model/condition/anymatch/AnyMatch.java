package com.achobeta.domain.recruit.model.condition.anymatch;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.domain.recruit.model.condition.Condition;
import com.achobeta.domain.student.model.entity.StuResume;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 22:14
 */
@Data
public class AnyMatch implements Condition {

    @IntRange(min = 1000, max = 9999, message = "年级应均为四位数")
    private GradeCondition grade; // 允许的特定年级

//    @NotNull(message = "面向的学生 id 列表可以为空集合但不能为 null")
    private UserIdCondition userId; // 允许的特定学生

    @Override
    public Predicate<StuResume> predicate() {
        Predicate<StuResume> predicate = stuResume -> Boolean.FALSE;
        // 取以上属性条件的并集
        return predicate.or(skipOrIfNull(grade))
                .or(skipOrIfNull(userId));
    }

}
