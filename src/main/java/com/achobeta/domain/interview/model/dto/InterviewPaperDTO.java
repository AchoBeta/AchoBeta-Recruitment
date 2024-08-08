package com.achobeta.domain.interview.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 12:57
 */
@Data
public class InterviewPaperDTO {

    @NotNull(message = "面试 id 不能为空")
    private Long interviewId;

    @NotNull(message = "面试试卷 id 不能为空")
    private Long paperId;

}
