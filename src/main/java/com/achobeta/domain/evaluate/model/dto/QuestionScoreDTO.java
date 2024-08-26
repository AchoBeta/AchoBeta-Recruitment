package com.achobeta.domain.evaluate.model.dto;

import com.achobeta.common.annotation.IntRange;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    @IntRange(min = -1, max = 10, message = "题目得分数值范围为 -1-10，其中 -1 代表超纲")
    private Integer score;

}
