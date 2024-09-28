package com.achobeta.domain.schedule.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.achobeta.config.DateTimeConfig;
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

    @ExcelIgnore
    private Long id;

    @ExcelIgnore
    private Long participationId;

    @Excel(name = "开始时间", width = 20, format = DateTimeConfig.DATE_TIME_PATTERN, needMerge = true)
    private Date startTime;

    @Excel(name = "结束时间", width = 20, format = DateTimeConfig.DATE_TIME_PATTERN, needMerge = true)
    private Date endTime;

}
