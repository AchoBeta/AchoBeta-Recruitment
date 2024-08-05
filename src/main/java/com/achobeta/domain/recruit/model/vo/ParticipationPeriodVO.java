package com.achobeta.domain.recruit.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 11:09
 */
@Data
public class ParticipationPeriodVO {

    private Long id;

    private List<TimePeriodVO> timePeriodVOS;

}
