package com.achobeta.domain.evaluate.model.dto;

import com.achobeta.common.annotation.IntRange;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.achobeta.domain.evaluate.constants.InterviewEvaluateConstants.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 1:50
 */
@Data
public class QuestionScoreDTO {

    @NotNull(message = "面试 id 不能为空")
    private Long interviewId;

    @NotNull(message = "问题 id 不能为空")
    private Long questionId;

    @IntRange(min = SUPERCLASS_QUESTION_SCORE, max = MAX_QUESTION_SCORE, message = QUESTION_SCORE_MESSAGE)
    private Integer score;

}
