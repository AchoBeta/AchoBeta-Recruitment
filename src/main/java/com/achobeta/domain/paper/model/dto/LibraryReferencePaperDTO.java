package com.achobeta.domain.paper.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-16
 * Time: 20:43
 */
@Data
public class LibraryReferencePaperDTO {

    @NotNull(message = "试卷库 id 不能为空")
    private Long libId;

    @NotEmpty(message = "试卷 id 列表不能为空")
    private List<Long> paperIds;

}
