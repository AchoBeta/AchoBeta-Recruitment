package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-12
 * Time: 0:19
 */
@Data
public class EntryDTO {

    @NotNull(message = "自定义项 id 不能为空")
    private Long entryId;

    @NotBlank(message = "自定义项内容不能为空")
    private String content;
}
