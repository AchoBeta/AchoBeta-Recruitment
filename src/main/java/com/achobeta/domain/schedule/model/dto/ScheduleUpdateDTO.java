package com.achobeta.domain.schedule.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 0:21
 */
@Data
public class ScheduleUpdateDTO {

    @NotNull(message = "面试预约 id 不能为空")
    private Long scheduleId;

    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    private Long endTime;

}
