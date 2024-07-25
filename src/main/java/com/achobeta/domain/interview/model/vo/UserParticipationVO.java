package com.achobeta.domain.interview.model.vo;

import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
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
public class UserParticipationVO {

    private Long participationId;

    private SimpleStudentVO simpleStudentVO;

    private List<TimePeriodVO> timePeriodVOS;

    private List<ScheduleVO> scheduleVOS;

}
