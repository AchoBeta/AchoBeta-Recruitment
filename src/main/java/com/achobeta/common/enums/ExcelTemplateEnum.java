package com.achobeta.common.enums;

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

    ;

    private final String title;

    private final String sheetName;

    private final String originalName;
}
