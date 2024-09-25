package com.achobeta.domain.evaluate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static com.achobeta.domain.evaluate.constants.InterviewEvaluateConstants.MAX_ABILITY_VALUE;
import static com.achobeta.domain.evaluate.constants.InterviewEvaluateConstants.MIN_ABILITY_VALUE;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 19:22
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSummaryTemplate {

    private final static String HIT_EMOJI = "\uD83D\uDE0E "; // 😎

    private final static String NOT_HIT_EMOJI = "\uD83E\uDEE5 "; // 🫥

    private String studentId;

    private String title;

    private Integer basis;

    private Integer coding;

    private Integer thinking;

    private Integer express;

    private String evaluate;

    private String suggest;

    private String playback;

    // 分数转化为 emoji 字符串（输入模板之前，用 java 控制输出字符串与处理边界情况，会更加方便）
    private static String getStars(Integer credit) {
        int num = Optional.ofNullable(credit)
                .filter(i -> i.compareTo(MIN_ABILITY_VALUE) >= 0 && i.compareTo(MAX_ABILITY_VALUE) <= 0)
                .orElse(0);
        // num 个命中，max - num 个未命中
        return HIT_EMOJI.repeat(num) + NOT_HIT_EMOJI.repeat(MAX_ABILITY_VALUE - num);
    }

    public String getBasis() {
        return getStars(this.basis);
    }

    public String getCoding() {
        return getStars(this.coding);
    }

    public String getThinking() {
        return getStars(this.thinking);
    }

    public String getExpress() {
        return getStars(this.express);
    }

}
