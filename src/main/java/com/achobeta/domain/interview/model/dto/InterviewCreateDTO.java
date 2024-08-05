package com.achobeta.domain.interview.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-05
 * Time: 23:56
 */
@Data
public class InterviewCreateDTO {

    @NotNull(message = "面试预约 id 不能为空")
    private Long scheduleId;

    @NotBlank(message = "面试标题不能为空")
    private String title;

    @NotBlank(message = "面试描述不能为空")
    private String description;

    @NotBlank(message = "面试地址不能为空")
    private String address;

}
