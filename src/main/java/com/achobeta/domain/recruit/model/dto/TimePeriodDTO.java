package com.achobeta.domain.recruit.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 18:10
 */
@Data
public class TimePeriodDTO {

    @NotNull(message = "招新活动 id 不能为空")
    private Long actId;

    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    private Long endTime;
}
