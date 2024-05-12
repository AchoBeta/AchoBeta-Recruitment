package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 13:49
 */
@Data
public class TimePeriodDTO {

    @NotNull(message = "招新 id 不能为空")
    private Long recId;

    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    private Long endTime;

}
