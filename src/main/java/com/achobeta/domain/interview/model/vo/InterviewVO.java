package com.achobeta.domain.interview.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.achobeta.common.enums.InterviewStatus;
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
@ExcelTarget("AchoBeta 面试日程")
public class InterviewVO {

    private Long id;

    @Excel(name = "标题", width = 20)//列名、宽度
    private String title;

    @Excel(name = "状态", width = 20)//列名、宽度
    private InterviewStatus status;

    @Excel(name = "时间", width = 100)//列名、宽度
    private ScheduleVO scheduleVO;

}
