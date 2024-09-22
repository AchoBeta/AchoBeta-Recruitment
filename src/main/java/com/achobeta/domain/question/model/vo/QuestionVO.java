package com.achobeta.domain.question.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-05
 * Time: 20:29
 */
@Data
public class QuestionVO {

    private Long id;

    private String title;

    private String standard;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public void hidden() {
        setStandard(null);
    }
}
