package com.achobeta.domain.evaluate.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 14:05
 */
@Data
public class InterviewCommentVO {

    private Long id;

    private InterviewCommentatorVO commentator;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
