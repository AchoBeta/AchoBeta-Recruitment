package com.achobeta.domain.qpaper.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 9:03
 */
@Data
public class PaperQuestionLinkDTO {

    @NotNull(message = "试卷 id 不能为空")
    private Long paperId;

    @NotNull(message = "问题 ids 不能为空")
    private List<Long> questionIds;

}
