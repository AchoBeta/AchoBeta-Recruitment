package com.achobeta.domain.recruit.model.json;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.domain.student.model.entity.StuResume;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 22:14
 */
@Data
public class AllMatchCondition {

    @NotNull(message = "面向的简历状态列表可以为空集合但不能为 null")
    List<Integer> status; // 允许的简历状态

    public Predicate<StuResume> predicate() {
        Predicate<StuResume> predicate = stuResume -> Boolean.TRUE;
        // 取以上属性条件的交集
        return predicate.and(stuResume -> status.contains(stuResume.getStatus().getCode()));
    }

}
