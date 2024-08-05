package com.achobeta.domain.interview.model.vo;

import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
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

    private InterviewStatusEnum status;

    private ScheduleVO scheduleVO;

}
