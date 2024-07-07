package com.achobeta.domain.recruit.model.vo;

import com.achobeta.domain.question.model.vo.QuestionVO;
import lombok.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 20:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentTemplate {

    private RecruitmentActivityVO recruitmentActivityVO;

    private List<QuestionVO> questionVOS;

    private List<TimePeriodVO> timePeriodVOS;

    public void hidden() {
        recruitmentActivityVO.hidden();
        questionVOS.forEach(QuestionVO::hidden);
    }
}