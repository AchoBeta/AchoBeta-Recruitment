package com.achobeta.domain.paper.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.model.dao.mapper.QuestionPaperLibraryMapper;
import com.achobeta.domain.paper.model.entity.QuestionPaperLibrary;
import com.achobeta.domain.paper.service.QuestionPaperLibraryService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper_library(试卷库表)】的数据库操作Service实现
* @createDate 2024-07-05 22:38:52
*/
@Service
public class QuestionPaperLibraryServiceImpl extends ServiceImpl<QuestionPaperLibraryMapper, QuestionPaperLibrary>
    implements QuestionPaperLibraryService{

    @Override
    public Optional<QuestionPaperLibrary> getPaperLibrary(Long libId) {
        return this.lambdaQuery()
                .eq(QuestionPaperLibrary::getId, libId)
                .oneOpt();
    }

    @Override
    public List<QuestionPaperLibrary> getPaperLibraries() {
        return this.lambdaQuery().list();
    }

    @Override
    public Long createPaperLibrary(String libType) {
        QuestionPaperLibrary questionPaperLibrary = new QuestionPaperLibrary();
        questionPaperLibrary.setLibType(libType);
        this.save(questionPaperLibrary);
        return questionPaperLibrary.getId();
    }

    @Override
    public void renamePaperLibrary(Long libId, String libType) {
        this.lambdaUpdate()
                .eq(QuestionPaperLibrary::getId, libId)
                .set(QuestionPaperLibrary::getLibType, libType)
                .update();
    }

    @Override
    public void checkPaperLibraryExists(Long libId) {
        getPaperLibrary(libId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_PAPER_LIBRARY_NOT_EXISTS));
    }
}




