package com.achobeta.domain.schedule.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-14
 * Time: 16:20
 */
@Data
public class ScheduleInterviewerVO extends ScheduleVO {

    private List<InterviewerVO> interviewerVOList;

}
