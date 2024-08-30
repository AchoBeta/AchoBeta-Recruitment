package com.achobeta.domain.recruit.model.entity;

import com.achobeta.common.annotation.IntRange;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 12:57
 *
 * 活动针对的对象，可以是特定的年级列表 grade 和特定的学生 id 列表 uid
 * grade 和 uid 可以是空集合，可以只针对某些年级或者只针对某些学生展开活动
 * 都为空集合需要管理员修改，否则没有学生能够看到这个活动
 */
@Data
public class StudentGroup {

    @IntRange(min = 1000, max = 9999, message = "年级列表应均为四位数")
    List<Integer> grade; // 特定年级

    @NotNull(message = "面向的学生 id 列表可以为空集合但不能为 null")
    List<Long> uid; // 特定学生

}
