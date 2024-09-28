package com.achobeta.domain.interview.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-24
 * Time: 10:10
 */
@Data
public class InterviewExcelTemplate {

    @Excel(name = "标题", width = 50)
    private String title;

    @Excel(name = "状态", width = 20)
    private InterviewStatus status;

    @ExcelEntity(name = "面试预约")
    private ScheduleVO scheduleVO;

}
