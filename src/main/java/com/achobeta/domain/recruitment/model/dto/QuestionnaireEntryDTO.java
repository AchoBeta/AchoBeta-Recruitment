package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 17:49
 */
@Data
public class QuestionnaireEntryDTO {

    @NotNull(message = "问卷 id 不能为空")
    private Long questionnaireId;

    @NotNull(message = "自定义项 id 不能为空")
    private Long entryId;

    @NotBlank(message = "自定义项内容不能为空")
    private String content;

}
