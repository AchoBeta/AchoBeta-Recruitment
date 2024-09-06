package com.achobeta.domain.recruit.model.condition.allmatch;

import com.achobeta.common.annotation.Intercept;
import com.achobeta.domain.recruit.model.condition.Condition;
import com.achobeta.domain.student.model.entity.StuResume;
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
public class AllMatch implements Condition {

//    @NotNull(message = "面向的学生简历状态列表可以为空集合但不能为 null")
    private StatusCondition status; // 允许的简历状态

    @Override
    public Predicate<StuResume> predicate() {
        Predicate<StuResume> predicate = stuResume -> Boolean.TRUE;
        // 取以上属性条件的交集
        return predicate.and(skipAndIfNull(status));
    }

}
