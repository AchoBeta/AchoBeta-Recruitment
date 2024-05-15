package com.achobeta.domain.paper.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.handler.chain.RemovePaperHandlerChain;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.paper.model.dao.mapper.QuestionPaperMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper(问题清单表)】的数据库操作Service实现
* @createDate 2024-05-14 23:32:01
*/
@Service
@RequiredArgsConstructor
public class QuestionPaperServiceImpl extends ServiceImpl<QuestionPaperMapper, QuestionPaper>
    implements QuestionPaperService{

    private final QuestionPaperMapper questionPaperMapper;

    @Override
    public List<QuestionPaperVO> getQuestionPapers(Long libId) {
        return questionPaperMapper.getQuestionPapers(libId);
    }

    @Override
    public Optional<QuestionPaper> getQuestionPaper(Long paperId) {
        return this.lambdaQuery()
                .eq(QuestionPaper::getId, paperId)
                .oneOpt();
    }

    @Override
    public Long addQuestionPaper(Long libId, String title) {
        QuestionPaper questionPaper = new QuestionPaper();
        questionPaper.setLibId(libId);
        questionPaper.setTitle(title);
        this.save(questionPaper);
        return questionPaper.getId();
    }

    @Override
    public void renamePaperTitle(Long paperId, String title) {
        this.lambdaUpdate()
                .eq(QuestionPaper::getId, paperId)
                .set(QuestionPaper::getTitle, title)
                .update();
    }

    @Override
    public void removeQuestionPaper(Long paperId) {
        this.lambdaUpdate()
                .eq(QuestionPaper::getId, paperId)
                .remove();
    }

    @Override
    public void checkPaperExists(Long paperId) {
        getQuestionPaper(paperId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_PAPER_NOT_EXISTS));
    }
}




