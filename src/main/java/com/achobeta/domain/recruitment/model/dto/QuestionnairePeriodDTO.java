package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 17:50
 */
@Data
public class QuestionnairePeriodDTO {

    @NotNull(message = "问卷 id 不能为空")
    private Long questionnaireId;

    @NotNull(message = "时间段 id 不能为空")
    private Long periodId;

}
