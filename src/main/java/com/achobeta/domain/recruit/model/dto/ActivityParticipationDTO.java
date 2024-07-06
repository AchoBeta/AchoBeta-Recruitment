package com.achobeta.domain.recruit.model.dto;

import com.achobeta.domain.question.model.dto.QuestionDTO;
import com.achobeta.domain.recruitment.model.dto.QuestionnaireEntryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 20:12
 */
@Data
public class ActivityParticipationDTO {

    @NotNull(message = "“活动参与” id 不能为空")
    private Long participationId;

    @NotNull(message = "问题列表不能为 null")
    private List<QuestionAnswerDTO> questionAnswerDTOS;

    @NotNull(message = "选中时间段的 id 集合不能为 null")
    private List<Long> periodIds;
}
