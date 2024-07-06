package com.achobeta.domain.recruit.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 18:23
 */
@Data
public class RecruitmentBatchUpdateDTO {

    @NotNull(message = "招新批次 id 不能为空")
    private Long batchId;

    @NotBlank(message = "招新标题不能为空")
    private String title;

    @NotNull(message = "截止时间不能为空")
    private Long deadline;
}
