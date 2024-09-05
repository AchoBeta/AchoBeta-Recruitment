package com.achobeta.domain.recruit.model.json;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.domain.student.model.entity.StuResume;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 22:14
 */
@Data
public class AnyMatchCondition {

    @IntRange(min = 1000, max = 9999, message = "年级列表应均为四位数")
    List<Integer> grade; // 允许的特定年级

    @NotNull(message = "面向的学生 id 列表可以为空集合但不能为 null")
    List<Long> uid; // 允许的特定学生

    public Predicate<StuResume> predicate() {
        Predicate<StuResume> predicate = stuResume -> Boolean.FALSE;
        // 取以上属性条件的并集
        return predicate.or(stuResume -> grade.contains(stuResume.getGrade()))
                .or(stuResume -> uid.contains(stuResume.getUserId()));
    }

}
