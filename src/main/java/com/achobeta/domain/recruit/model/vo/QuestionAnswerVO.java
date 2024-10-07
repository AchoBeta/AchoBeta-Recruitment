package com.achobeta.domain.recruit.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 13:29
 */
@Data
public class QuestionAnswerVO {

    @ExcelIgnore
    private Long id;

    @Excel(name = "问题", width = 50, needMerge = true)
    private String title;

    // height 最好不要设置，容易出 bug
    @Excel(name = "回答", width = 50, needMerge = true)
    private String answer;
}
