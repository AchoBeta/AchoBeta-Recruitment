package com.achobeta.domain.recruit.model.vo;

import lombok.Data;

import java.util.Date;


/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 13:27
 */
@Data
public class TimePeriodVO {

    private Long id;

    private Date startTime;

    private Date endTime;
}
