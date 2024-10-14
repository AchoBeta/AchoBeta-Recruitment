package com.achobeta.domain.interview.model.vo;

import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 1:52
 */
@Data
public class InterviewVO {

    private Long id;

    private String title;

    private InterviewStatus status;

    private ScheduleVO scheduleVO;

    private SimpleStudentVO simpleStudentVO;

}
