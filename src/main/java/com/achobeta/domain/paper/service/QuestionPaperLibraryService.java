package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.entity.QuestionLibrary;
import com.achobeta.domain.paper.model.entity.QuestionPaperLibrary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper_library(题单仓库表)】的数据库操作Service
* @createDate 2024-05-14 23:32:01
*/
public interface QuestionPaperLibraryService extends IService<QuestionPaperLibrary> {

    Optional<QuestionPaperLibrary> getPaperLibrary(Long libId);

    List<QuestionPaperLibrary> getPaperLibraries();

    Long createPaperLibrary(String libType);

    void renamePaperLibrary(Long libId, String libType);

    void checkPaperLibraryExists(Long libId);

}
