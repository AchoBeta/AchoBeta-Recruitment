package com.achobeta.domain.recruitment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-12
 * Time: 11:03
 */
@Data
public class RecruitmentDTO {

    @NotNull(message = "AB版本不能为空")
    private Integer batch;

    @NotNull(message = "自定义项内容不能为空")
    private Long deadline;

}
