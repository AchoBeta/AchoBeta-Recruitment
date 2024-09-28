package com.achobeta.domain.schedule.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.achobeta.domain.recruit.model.vo.QuestionAnswerVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
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

    @Excel(name = "年级", width = 20, needMerge = true)
    private Integer grade;

    @Excel(name = "专业", width = 20, needMerge = true)
    private String major;

    @Excel(name = "班级", width = 20, needMerge = true)
    private String className;

    @Excel(name = "性别", width = 20, replace = {"男_0", "女_1"}, needMerge = true)
    private Integer gender;

    @Excel(name = "邮箱", width = 20, needMerge = true)
    private String email;

    @Excel(name = "手机号码", width = 20, needMerge = true)
    private String phoneNumber;

    @Excel(name = "加入 AchoBeta 的理由", width = 50, needMerge = true)
    private String reason;

    @Excel(name = "个人介绍（自我认知）", width = 50, needMerge = true)
    private String introduce;

    @Excel(name = "个人经历 （项目经历、 职业规划等）", width = 50, needMerge = true)
    private String experience;

    @Excel(name = "获奖经历", width = 50, needMerge = true)
    private String awards;

    @Excel(name = "备注", width = 50, needMerge = true)
    private String remark;

    @Excel(name = "简历状态", width = 20, needMerge = true)
    private ResumeStatus status;

    @Excel(name = "已提交次数", width = 20, needMerge = true)
    private Integer submitCount;

    @ExcelCollection(name = "Q&A")
    private List<QuestionAnswerVO> questionAnswerVOS;

    @ExcelCollection(name = "空闲时间")
    private List<TimePeriodVO> timePeriodVOS;

    @ExcelCollection(name = "已有预约")
    private List<ScheduleVO> scheduleVOS;

}
