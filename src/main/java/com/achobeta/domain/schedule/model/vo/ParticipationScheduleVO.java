package com.achobeta.domain.schedule.model.vo;

import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 17:56
 */
@Data
public class ParticipationScheduleVO {

    private Long participationId;

    private SimpleStudentVO simpleStudentVO;

    private List<ScheduleVO> scheduleVOS;

}
