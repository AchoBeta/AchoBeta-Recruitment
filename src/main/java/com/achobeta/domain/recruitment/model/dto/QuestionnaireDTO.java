package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-12
 * Time: 0:17
 */
@Data
public class QuestionnaireDTO {

    @NotNull(message = "问卷 id 不能为空")
    private Long questionnaireId;

    @NotNull(message = "自定义项集合不能为 null")
    private List<EntryDTO> entryDTOS; // 自定义项集合

    @NotNull(message = "选中时间段的 id 集合不能为 null")
    private List<Long> periodIds; // 选中时间段的 id 集合

}
