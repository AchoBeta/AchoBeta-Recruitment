package com.achobeta.domain.question.model.dto;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 13:48
 */
@Data
public class QuestionQueryDTO {

    private Integer current;

    private Integer pageSize;

    private List<Long> libIds;

}
