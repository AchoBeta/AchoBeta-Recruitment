package com.achobeta.domain.interview.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import com.achobeta.util.TimeUtil;
import lombok.Data;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-24
 * Time: 10:10
 */
@Data
@ExcelTarget("AchoBeta 面试日程")
public class InterviewExcelTemplate {

    @Excel(name = "标题", width = 50)//列名、宽度
    private String title;

    @Excel(name = "状态", width = 20)//列名、宽度
    private InterviewStatus status;

    @Excel(name = "时间", width = 100)//列名、宽度
    private ScheduleVO scheduleVO;

    public String getScheduleVO() {
        return Optional.ofNullable(scheduleVO).map(schedule -> {
            return String.format(
                    "%s —— %s",
                    TimeUtil.getDateTime(schedule.getStartTime()),
                    TimeUtil.getDateTime(schedule.getEndTime())
            );
        }).orElse(null);
    }
}
