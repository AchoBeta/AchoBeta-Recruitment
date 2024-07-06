package com.achobeta.domain.recruit.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 13:26
 */
@Data
public class ParticipationVO {

    private Long id;

    private Long stuId;

    private Long racId;

    private List<QuestionAnswerVO> questionAnswerVOS;

    private List<TimePeriodVO> timePeriodVOS;
}
