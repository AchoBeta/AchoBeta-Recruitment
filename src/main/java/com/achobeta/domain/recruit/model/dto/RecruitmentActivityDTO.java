package com.achobeta.domain.recruit.model.dto;

import com.achobeta.domain.recruit.model.condition.StudentGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 19:29
 */
@Data
public class RecruitmentActivityDTO {

    @NotNull(message = "招新批次 id 不能为空")
    private Long batchId;

    @Valid // 开启嵌套校验
    @NotNull(message = "面向人群不能为空")
    private StudentGroup target;

    @NotBlank(message = "活动标题不能为空")
    private String title;

    @NotBlank(message = "活动描述不能为空")
    private String description;

    @NotNull(message = "截止时间不能为空")
    private Long deadline;

}
