package com.achobeta.domain.recruit.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 13:35
 */
@Data
public class QuestionAnswerDTO {

    @NotNull(message = "问题 id 不能为空")
    private Long questionId;

//    @NotBlank(message = "回答不能为空")
    private String answer;
}
