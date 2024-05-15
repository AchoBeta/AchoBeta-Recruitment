package com.achobeta.domain.paper.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 1:57
 */
@Data
public class QuestionPaperDTO {

    @NotNull(message = "题单的 id 不能为空")
    private Long paperId;

    @NotBlank(message = "题单名不能为空")
    private String title;

}
