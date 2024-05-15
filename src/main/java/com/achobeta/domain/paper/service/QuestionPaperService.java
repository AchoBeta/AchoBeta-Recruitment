package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.entity.QuestionEntry;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper(问题清单表)】的数据库操作Service
* @createDate 2024-05-14 23:32:01
*/
public interface QuestionPaperService extends IService<QuestionPaper> {

    List<QuestionPaperVO> getQuestionPapers(Long libId);

    Optional<QuestionPaper> getQuestionPaper(Long paperId);

    Long addQuestionPaper(Long libId, String title);

    void renamePaperTitle(Long paperId, String title);

    void removeQuestionPaper(Long paperId);

    void checkPaperExists(Long paperId);

}
