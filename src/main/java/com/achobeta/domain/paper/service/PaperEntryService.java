package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.entity.PaperEntry;
import com.achobeta.domain.paper.model.entity.QuestionEntry;
import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【paper_entry(题单-题目关联表)】的数据库操作Service
* @createDate 2024-05-14 23:32:01
*/
public interface PaperEntryService extends IService<PaperEntry> {

    Optional<PaperEntry> getPaperEntry(Long paperId, Long questionId);

    void addQuestionForPaper(Long paperId, Long questionId);

    void removeQuestionFromPaper(Long paperId, Long questionId);

    PaperQuestionsVO getQuestionsOnPaper(Long paperId);

}
