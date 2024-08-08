package com.achobeta.domain.interview.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 0:03
 */
@Data
public class InterviewUpdateDTO {

    @NotNull(message = "面试 id 不能为空")
    private Long interviewId;

    @NotBlank(message = "面试标题不能为空")
    private String title;

    @NotBlank(message = "面试描述不能为空")
    private String description;

    @NotBlank(message = "面试地址不能为空")
    private String address;

}
