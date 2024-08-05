package com.achobeta.domain.schedule.model.vo;

import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 2:56
 */
@Data
public class UserParticipationVO extends ParticipationScheduleVO {

    private List<TimePeriodVO> timePeriodVOS;

}
