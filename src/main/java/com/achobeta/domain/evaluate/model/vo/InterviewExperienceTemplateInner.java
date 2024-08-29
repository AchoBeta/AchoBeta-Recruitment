package com.achobeta.domain.evaluate.model.vo;

import com.achobeta.common.enums.TextStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

import static com.achobeta.domain.evaluate.constants.InterviewEvaluateConstants.*;

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

    private Double average;

    private String standard;

    private TextStyle style;

    public String getScore() {
        return Optional.ofNullable(this.score)
                .map(i -> i.compareTo(SUPERCLASS_QUESTION_SCORE) <= 0 ? SUPERCLASS_MESSAGE : String.valueOf(this.score))
                .orElse(null);
    }

    private static TextStyle getStyle(Integer score) {
        return Optional.ofNullable(score)
                .map(i -> {
                    if (i.compareTo(MIN_QUESTION_SCORE) < 0) {
                        return TextStyle.GREY;
                    } else if (i.compareTo(PASS_QUESTION_SCORE) < 0) {
                        return TextStyle.RED;
                    } else {
                        return TextStyle.GREEN;
                    }
                }).orElse(null);
    }

    public String getStyle() {
        return Optional.ofNullable(this.style)
                .map(TextStyle::getStyle)
                .or(() -> {
                    return Optional.ofNullable(getStyle(this.score))
                            .map(TextStyle::getStyle);
                }).orElse(null);
    }

    public static class InterviewExperienceTemplateInnerBuilder {

        public InterviewExperienceTemplateInnerBuilder score(Integer score) {
            this.score = score;
            if(Objects.isNull(this.style)) {
                this.style = getStyle(score);
            }
            return this;
        }

    }

}