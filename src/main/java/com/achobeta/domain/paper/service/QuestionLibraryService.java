package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.entity.QuestionLibrary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_library(题目仓库表)】的数据库操作Service
* @createDate 2024-05-14 23:32:01
*/
public interface QuestionLibraryService extends IService<QuestionLibrary> {

    Optional<QuestionLibrary> getQuestionLibrary(Long libId);

    List<QuestionLibrary> getQuestionLibraries();

    Long createQuestionLibrary(String libType);

    void renameQuestionLibrary(Long libId, String libType);

    void checkQuestionLibraryExists(Long libId);

}
