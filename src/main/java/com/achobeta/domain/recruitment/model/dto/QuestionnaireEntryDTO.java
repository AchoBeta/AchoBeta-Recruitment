package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-12
 * Time: 0:19
 */
@Data
public class QuestionnaireEntryDTO {

    private Long entryId;

    private String answer;
}
