package com.achobeta.domain.question.service;

import com.achobeta.domain.question.model.dto.QuestionSaveBatchDTO;
import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_library(题库表)】的数据库操作Service
* @createDate 2024-07-05 20:03:35
*/
public interface QuestionLibraryService extends IService<QuestionLibrary> {

    Optional<QuestionLibrary> getQuestionLibrary(Long libId);

    List<QuestionLibrary> getQuestionLibraries();

    Long createQuestionLibrary(String libType);

    void renameQuestionLibrary(Long libId, String libType);

    void checkQuestionLibraryExists(Long libId);

}
