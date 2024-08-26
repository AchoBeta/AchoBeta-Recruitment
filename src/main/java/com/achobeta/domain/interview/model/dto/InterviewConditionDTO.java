package com.achobeta.domain.interview.model.dto;

import lombok.Data;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 19:15
 */
@Data
public class InterviewConditionDTO {

//    @NotNull(message = "招新批次 id 不能为空")
    private Long batchId;

//    @NotNull(message = "招新活动 id 不能为空")
    private Long actId;

    public static InterviewConditionDTO getCondition(InterviewConditionDTO interviewConditionDTO) {
        return Optional.ofNullable(interviewConditionDTO).orElse(new InterviewConditionDTO());
    }
}
