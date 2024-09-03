package com.achobeta.domain.schedule.model.vo;

import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 1:38
 */
@Data
public class ScheduleResumeVO extends ScheduleVO {

    private SimpleStudentVO simpleStudentVO;

}
