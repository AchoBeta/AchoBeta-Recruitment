package com.achobeta.domain.question.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 0:25
 */
@Data
public class QuestionDetailVO extends QuestionVO {

    private List<QuestionLibraryVO> types;

}
