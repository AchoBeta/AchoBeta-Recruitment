package com.achobeta.domain.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-24
 * Time: 1:39
 */
@Getter
@AllArgsConstructor
public enum ExcelTemplateEnum {

    ACHOBETA_INTERVIEW_ALL("AchoBeta 招新面试", "全部面试日程", "achobeta-interview-all.xlsx"),

    ACHOBETA_ACTIVITY_SITUATIONS("AchoBeta 招新活动参与情况", "所有参与者的参与情况", "achobeta-activity-situations.xlsx"),

    ;

    private final String title;

    private final String sheetName;

    private final String originalName;
}
