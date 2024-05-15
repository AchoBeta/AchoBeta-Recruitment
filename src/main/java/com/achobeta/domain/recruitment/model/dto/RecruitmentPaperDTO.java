package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 12:08
 */
@Data
public class RecruitmentPaperDTO {

    @NotNull(message = "招新 id 不能为空")
    private Long recId;

    @NotNull(message = "题单 id 不能为空")
    private Long paperId;

}
