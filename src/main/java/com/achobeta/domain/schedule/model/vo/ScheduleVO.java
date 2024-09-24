package com.achobeta.domain.schedule.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 2:23
 */
@Data
public class ScheduleVO {

    private Long id;

    private Long participationId;

    private Date startTime;

    private Date endTime;

}
