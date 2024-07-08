package com.achobeta.domain.recruit.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 19:53
 */
@Data
public class ActivityPaperDTO {

    @NotNull(message = "招新活动 id 不能为空")
    private Long actId;

    @NotNull(message = "试卷 id 不能为空")
    private Long paperId;

}
