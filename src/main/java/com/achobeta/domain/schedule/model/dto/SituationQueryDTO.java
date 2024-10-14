package com.achobeta.domain.schedule.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-15
 * Time: 0:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationQueryDTO {

    @NotNull(message = "活动 id 不能为空")
    private Long actId;

//    @NotEmpty(message = "状态列表不能为空")
    private List<Integer> statusList;

}
