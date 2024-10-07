package com.achobeta.domain.question.model.dto;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-06
 * Time: 2:56
 */
@Data
public class QuestionSaveBatchDTO {

    @Valid
    private List<QuestionLibraryDTO> libraries;

    @Valid
    private List<QuestionDTO> questions;

}
