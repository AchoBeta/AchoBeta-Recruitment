package com.achobeta.domain.paper.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-05
 * Time: 22:50
 */
@Data
public class QuestionPaperVO {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
