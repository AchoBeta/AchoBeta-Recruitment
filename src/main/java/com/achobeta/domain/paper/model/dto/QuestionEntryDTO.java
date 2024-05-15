package com.achobeta.domain.paper.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 1:50
 */
@Data
public class QuestionEntryDTO {

    @NotNull(message = "问题的 id 不能为空")
    private Long questionId;

    @NotBlank(message = "题目名不能为空")
    private String title;

}
