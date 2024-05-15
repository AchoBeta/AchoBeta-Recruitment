package com.achobeta.domain.paper.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 2:06
 */
@Data
public class PaperEntryDTO {

    @NotNull(message = "题单的 id 不能为空")
    private Long paperId;

    @NotNull(message = "题目的 id 不能为空")
    private Long questionId;

}
