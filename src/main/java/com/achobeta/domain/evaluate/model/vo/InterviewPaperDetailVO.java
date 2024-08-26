package com.achobeta.domain.evaluate.model.vo;

import com.achobeta.domain.paper.model.vo.PaperLibraryVO;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 12:34
 */
@Data
public class InterviewPaperDetailVO extends QuestionPaperVO {

    private List<PaperLibraryVO> types;

    private List<InterviewQuestionDetailVO> questions;

}
