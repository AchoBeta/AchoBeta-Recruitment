package com.achobeta.domain.interview.model.vo;

import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import lombok.Data;

import java.util.Date;

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
