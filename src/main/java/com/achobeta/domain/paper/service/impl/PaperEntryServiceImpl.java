package com.achobeta.domain.paper.service.impl;

import com.achobeta.domain.paper.handler.chain.RemoveQuestionHandlerChain;
import com.achobeta.domain.paper.model.entity.QuestionEntry;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.paper.model.entity.PaperEntry;
import com.achobeta.domain.paper.service.PaperEntryService;
import com.achobeta.domain.paper.model.dao.mapper.PaperEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【paper_entry(题单-题目关联表)】的数据库操作Service实现
* @createDate 2024-05-14 23:32:01
*/
@Service
@RequiredArgsConstructor
public class PaperEntryServiceImpl extends ServiceImpl<PaperEntryMapper, PaperEntry>
    implements PaperEntryService{

    private final PaperEntryMapper paperEntryMapper;

    @Override
    public Optional<PaperEntry> getPaperEntry(Long paperId, Long questionId) {
        return this.lambdaQuery()
                .eq(PaperEntry::getPaperId, paperId)
                .eq(PaperEntry::getQuestionId, questionId)
                .oneOpt();
    }

    @Override
    public void addQuestionForPaper(Long paperId, Long questionId) {
        getPaperEntry(paperId, questionId).ifPresentOrElse(paperEntry -> {
        }, () -> {
            PaperEntry paperEntry = new PaperEntry();
            paperEntry.setPaperId(paperId);
            paperEntry.setQuestionId(questionId);
            this.save(paperEntry);
        });
    }

    @Override
    public void removeQuestionFromPaper(Long paperId, Long questionId) {
        this.lambdaUpdate()
                .eq(PaperEntry::getPaperId, paperId)
                .eq(PaperEntry::getQuestionId, questionId)
                .remove();
    }

    @Override
    public PaperQuestionsVO getQuestionsOnPaper(Long paperId) {
        return paperEntryMapper.getQuestionsOnPaper(paperId);
    }
}




