package com.achobeta.domain.interview.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 18:18
 */
@Data
public class ScheduleDetailVO extends ScheduleResumeVO {

    private List<InterviewerVO> interviewerVOS;

}
