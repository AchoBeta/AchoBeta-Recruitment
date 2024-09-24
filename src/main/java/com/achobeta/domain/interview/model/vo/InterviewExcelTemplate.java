package com.achobeta.domain.interview.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.config.DateTimeConfig;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.util.Optional;

import static com.achobeta.config.DateTimeConfig.DATE_TIME_FORMAT;

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
                    DATE_TIME_FORMAT.format(schedule.getStartTime()),
                    DATE_TIME_FORMAT.format(schedule.getEndTime())
            );
        }).orElse(null);
    }
}
