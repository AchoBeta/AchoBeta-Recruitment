package com.achobeta.domain.qpaper.model.dao.mapper;

import com.achobeta.domain.qpaper.model.entity.QuestionPaperLibrary;
import com.achobeta.domain.qpaper.model.vo.QuestionPaperDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 马拉圈
* @description 针对表【question_paper_library(试卷库表)】的数据库操作Mapper
* @createDate 2024-07-05 22:38:52
* @Entity com.achobeta.domain.qpaper.model.entity.QuestionPaperLibrary
*/
public interface QuestionPaperLibraryMapper extends BaseMapper<QuestionPaperLibrary> {

    QuestionPaperDetailVO getQuestionPaperDetail(@Param("paperId") Long paperId);

}




