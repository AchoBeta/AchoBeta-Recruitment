package com.achobeta.domain.recruit.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.achobeta.config.DateTimeConfig;
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

    @ExcelIgnore
    private Long id;

    @Excel(name = "开始时间", width = 20, format = DateTimeConfig.DATE_TIME_PATTERN, needMerge = true)
    private Date startTime;

    @Excel(name = "结束时间", width = 20, format = DateTimeConfig.DATE_TIME_PATTERN, needMerge = true)
    private Date endTime;
}
