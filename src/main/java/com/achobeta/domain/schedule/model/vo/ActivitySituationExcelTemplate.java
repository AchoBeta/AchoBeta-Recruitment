package com.achobeta.domain.schedule.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.achobeta.domain.recruit.model.vo.QuestionAnswerVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.util.ObjectUtil;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 0:23
 */
@Data
public class ActivitySituationExcelTemplate {

    @Excel(name = "学号", width = 20, needMerge = true)
    private String studentId;

    @Excel(name = "姓名", width = 20, needMerge = true)
    private String name;

    @Excel(name = "用户名", width = 20, needMerge = true)
    private String username;

    @Excel(name = "邮箱", width = 20, needMerge = true)
    private String email;

    @Excel(name = "性别", width = 20, replace = {"男_0", "女_1"}, needMerge = true)
    private Integer gender;

    @Excel(name = "年纪", width = 20, needMerge = true)
    private Integer grade;

    @Excel(name = "专业", width = 20, needMerge = true)
    private String major;

    @Excel(name = "班级", width = 20, needMerge = true)
    private String className;

    @Excel(name = "简历状态", width = 20, needMerge = true)
    private ResumeStatus status;

    @ExcelCollection(name = "Q&A")
    private List<QuestionAnswerVO> questionAnswerVOS;

    @ExcelCollection(name = "空闲时间")
    private List<TimePeriodVO> timePeriodVOS;

    @ExcelCollection(name = "已有预约")
    private List<ScheduleVO> scheduleVOS;

}
