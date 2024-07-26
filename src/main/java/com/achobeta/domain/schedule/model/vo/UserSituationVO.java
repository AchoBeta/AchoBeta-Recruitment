package com.achobeta.domain.schedule.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 4:00
 */
@Data
public class UserSituationVO {

    private List<UserParticipationVO> userParticipationVOS;

    private List<TimePeriodCountVO> timePeriodCountVOS;

}
