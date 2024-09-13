package com.achobeta.domain.question.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 0:55
 */
@Data
public class QuestionLibraryVO {

    private Long id;

    private String libType;

    private LocalDateTime createTime;
}
