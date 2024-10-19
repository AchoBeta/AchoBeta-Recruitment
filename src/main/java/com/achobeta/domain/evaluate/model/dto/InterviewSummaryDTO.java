package com.achobeta.domain.evaluate.model.dto;

import com.achobeta.common.annotation.Accessible;
import com.achobeta.common.annotation.HttpUrl;
import com.achobeta.common.annotation.IntRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.achobeta.domain.evaluate.constants.InterviewEvaluateConstants.*;

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

    @NotNull(message = "数值不能为空")
    @IntRange(min = MIN_ABILITY_VALUE, max = MAX_ABILITY_VALUE, message = "基础" + ABILITY_VALUE_MESSAGE)
    private Integer basis;

    @NotNull(message = "数值不能为空")
    @IntRange(min = MIN_ABILITY_VALUE, max = MAX_ABILITY_VALUE, message = "代码" + ABILITY_VALUE_MESSAGE)
    private Integer coding;

    @NotNull(message = "数值不能为空")
    @IntRange(min = MIN_ABILITY_VALUE, max = MAX_ABILITY_VALUE, message = "思维" + ABILITY_VALUE_MESSAGE)
    private Integer thinking;

    @NotNull(message = "数值不能为空")
    @IntRange(min = MIN_ABILITY_VALUE, max = MAX_ABILITY_VALUE, message = "表达" + ABILITY_VALUE_MESSAGE)
    private Integer express;

    @NotBlank(message = "总评不能为空")
    private String evaluate;

//    @NotBlank(message = "建议不能为空")
    private String suggest;

//    @NotBlank(message = "回放不能为空")
    @HttpUrl
    private String playback;

}
