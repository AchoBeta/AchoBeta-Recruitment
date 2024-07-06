package com.achobeta.domain.paper.model.dao.mapper;

import com.achobeta.domain.paper.model.entity.PaperQuestionLink;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【paper_question_link(试卷-问题关联表)】的数据库操作Mapper
* @createDate 2024-07-05 22:38:52
* @Entity com.achobeta.domain.qpaper.model.entity.PaperQuestionLink
*/
public interface PaperQuestionLinkMapper extends BaseMapper<PaperQuestionLink> {

    List<QuestionVO> getQuestionsOnPaper(@Param("paperId") Long paperId);

}




