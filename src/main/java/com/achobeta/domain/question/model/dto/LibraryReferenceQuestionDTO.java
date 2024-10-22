package com.achobeta.domain.question.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-16
 * Time: 20:22
 */
@Data
public class LibraryReferenceQuestionDTO {

    @NotNull(message = "题库 id 不能为空")
    private Long libId;

    @NotEmpty(message = "问题 id 列表不能为空")
    private List<Long> questionIds;

}
