package com.achobeta.domain.paper.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.paper.model.entity.QuestionLibrary;
import com.achobeta.domain.paper.service.QuestionLibraryService;
import com.achobeta.domain.paper.model.dao.mapper.QuestionLibraryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_library(题目仓库表)】的数据库操作Service实现
* @createDate 2024-05-14 23:32:01
*/
@Service
public class QuestionLibraryServiceImpl extends ServiceImpl<QuestionLibraryMapper, QuestionLibrary>
    implements QuestionLibraryService{

    @Override
    public Optional<QuestionLibrary> getQuestionLibrary(Long libId) {
        return this.lambdaQuery()
                .eq(QuestionLibrary::getId, libId)
                .oneOpt();
    }

    @Override
    public List<QuestionLibrary> getQuestionLibraries() {
        return this.lambdaQuery().list();
    }

    @Override
    public Long createQuestionLibrary(String libType) {
        QuestionLibrary questionLibrary = new QuestionLibrary();
        questionLibrary.setLibType(libType);
        this.save(questionLibrary);
        return questionLibrary.getId();
    }

    @Override
    public void renameQuestionLibrary(Long libId, String libType) {
        this.lambdaUpdate()
                .eq(QuestionLibrary::getId, libId)
                .set(QuestionLibrary::getLibType, libType)
                .update();
    }

    @Override
    public void checkQuestionLibraryExists(Long libId) {
        getQuestionLibrary(libId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_LIBRARY_NOT_EXISTS));
    }
}




