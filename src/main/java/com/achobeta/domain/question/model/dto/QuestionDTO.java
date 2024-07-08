package com.achobeta.domain.question.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 1:01
 */
@Data
public class QuestionDTO {

    @NotNull(message = "题库 ids 不能为空")
    private List<Long> libIds;

    @NotBlank(message = "题目不能为空")
    private String title;

    @NotBlank(message = "标答不能为空")
    private String standard;

}
