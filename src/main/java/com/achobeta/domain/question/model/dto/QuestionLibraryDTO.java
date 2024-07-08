package com.achobeta.domain.question.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 0:53
 */
@Data
public class QuestionLibraryDTO {

    @NotNull(message = "库的 id 不能为空")
    private Long libId;

    @NotBlank(message = "库的类型不能为空")
    private String libType;

}
