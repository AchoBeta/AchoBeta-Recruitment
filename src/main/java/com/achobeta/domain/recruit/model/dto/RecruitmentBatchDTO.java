package com.achobeta.domain.recruit.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 18:16
 */
@Data
public class RecruitmentBatchDTO {

    @NotNull(message = "招新届数不能为空")
    @Min(value = 1, message = "ab 版本非法")
    private Integer batch;

    @NotBlank(message = "招新标题不能为空")
    private String title;

    @NotNull(message = "截止时间不能为空")
    private Long deadline;

}
