package com.achobeta.domain.paper.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 0:56
 */
@Data
public class PaperLibraryVO {

    private Long id;

    private String libType;

    private LocalDateTime createTime;
}
