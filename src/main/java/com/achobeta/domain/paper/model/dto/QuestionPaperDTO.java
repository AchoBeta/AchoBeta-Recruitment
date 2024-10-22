package com.achobeta.domain.paper.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 8:35
 */
@Data
public class QuestionPaperDTO {

    @NotEmpty(message = "试卷库 ids 不能为空")
    private List<Long> libIds;

    @NotBlank(message = "题目不能为空")
    private String title;

    @NotBlank(message = "试卷说明不能为空")
    private String description;

}
