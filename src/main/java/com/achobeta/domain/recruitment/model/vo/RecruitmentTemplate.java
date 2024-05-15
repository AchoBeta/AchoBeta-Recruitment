package com.achobeta.domain.recruitment.model.vo;

import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import lombok.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 23:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentTemplate {

    private RecruitmentVO recruitmentVO;

    private List<QuestionEntryVO> questionEntryVOS;

    private List<TimePeriodVO> timePeriodVOS;
}
