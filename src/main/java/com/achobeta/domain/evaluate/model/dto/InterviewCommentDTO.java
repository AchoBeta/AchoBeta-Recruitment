package com.achobeta.domain.evaluate.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 14:37
 */
@Data
public class InterviewCommentDTO {

    @NotNull(message = "面试 id 不能为空")
    private Long interviewId;

    @NotBlank(message = "面试评论内容不能为空")
    private String content;

}
