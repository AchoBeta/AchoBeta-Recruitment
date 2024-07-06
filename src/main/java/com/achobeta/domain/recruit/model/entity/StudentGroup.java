package com.achobeta.domain.recruit.model.entity;

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

    List<Integer> grade; // 特定年级

    List<Long> uid; // 特定学生

}
