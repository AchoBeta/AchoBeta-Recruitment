package com.achobeta.domain.paper.model.dao.mapper;

import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【question_paper(试卷表)】的数据库操作Mapper
* @createDate 2024-07-05 22:38:52
* @Entity com.achobeta.domain.qpaper.model.entity.QuestionPaper
*/
public interface QuestionPaperMapper extends BaseMapper<QuestionPaper> {

    List<QuestionPaperVO> getQuestionPapersByLibId(@Param("libId") Long libId);

}




