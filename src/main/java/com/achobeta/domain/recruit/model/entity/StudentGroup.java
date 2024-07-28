package com.achobeta.domain.recruit.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 12:57
 */
@Data
public class StudentGroup {

    @NotNull(message = "面向的年级列表可以为空集合但不能为 null")
    List<Integer> grade; // 特定年级

    @NotNull(message = "面向的学生 id 列表可以为空集合但不能为 null")
    List<Long> uid; // 特定学生

}
