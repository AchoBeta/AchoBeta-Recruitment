package com.achobeta.domain.schedule.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-25
 * Time: 23:15
 */
@Data
public class ScheduleDTO {

    @NotNull(message = "用户活动参与票据不能为空")
    private Long participationId;

    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    private Long endTime;

}
