package com.achobeta.domain.evaluate.model.dto;

import com.achobeta.common.annotation.IntRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:14
 */
@Data
public class InterviewSummaryDTO {

    @NotNull(message = "面试 id 不能为空")
    private Long interviewId;

    @IntRange(min = 0, max = 5, message = "基础理论知识掌握数值范围为 0-5")
    private Integer basis;

    @IntRange(min = 0, max = 5, message = "代码能力数值范围为 0-5")
    private Integer coding;

    @IntRange(min = 0, max = 5, message = "思维能力数值范围为 0-5")
    private Integer thinking;

    @IntRange(min = 0, max = 5, message = "表达能力数值范围为 0-5")
    private Integer express;

    @NotBlank(message = "总评不能为空")
    private String evaluate;

//    @NotBlank(message = "建议不能为空")
    private String suggest;

//    @NotBlank(message = "回放不能为空")
    private String playback;

}
