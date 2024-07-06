package com.achobeta.domain.qpaper.service;

import com.achobeta.domain.qpaper.model.entity.QuestionPaperLibrary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper_library(试卷库表)】的数据库操作Service
* @createDate 2024-07-05 22:38:52
*/
public interface QuestionPaperLibraryService extends IService<QuestionPaperLibrary> {

    Optional<QuestionPaperLibrary> getPaperLibrary(Long libId);

    List<QuestionPaperLibrary> getPaperLibraries();

    Long createPaperLibrary(String libType);

    void renamePaperLibrary(Long libId, String libType);

    void checkPaperLibraryExists(Long libId);
}
