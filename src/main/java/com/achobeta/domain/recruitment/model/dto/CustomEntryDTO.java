package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 15:34
 */
@Data
public class CustomEntryDTO {

    @NotNull(message = "招新 id 不能为空")
    private Long recId;

    @NotBlank(message = "自定义项名不能为空")
    private String title;
}
