package com.achobeta.domain.paper.model.vo;

import com.achobeta.domain.question.model.vo.QuestionVO;
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
public class QuestionPaperDetailVO extends QuestionPaperVO {

    private List<String> types;

    private List<QuestionVO> questions;

}
