package com.achobeta.domain.evaluate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 12:35
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewExperienceTemplateInner {

    private String title;

    private Integer score;

    private String standard;

    private Double average;

    public String getScore() {
        return Integer.valueOf(-1).equals(this.score) ? "超纲" : String.valueOf(this.score);
    }
}
