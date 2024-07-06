package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.entity.PaperEntry;
import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.achobeta.domain.paper.model.entity.PaperQuestionLink;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【paper_question_link(试卷-问题关联表)】的数据库操作Service
* @createDate 2024-07-05 22:38:52
*/
public interface PaperQuestionLinkService extends IService<PaperQuestionLink> {

    List<QuestionVO> getQuestionsOnPaper(Long paperId);

    Optional<PaperQuestionLink> getPaperQuestionLink(Long paperId, Long questionId);

    void addQuestionsForPaper(Long paperId, List<Long> questionIds);

    void removeQuestionsFromPaper(Long paperId, List<Long> questionIds);

    QuestionPaperDetailVO getPaperDetail(Long paperId);

}
