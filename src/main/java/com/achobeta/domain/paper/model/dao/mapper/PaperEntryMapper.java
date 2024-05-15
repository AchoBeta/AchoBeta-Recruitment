package com.achobeta.domain.paper.model.dao.mapper;

import com.achobeta.domain.paper.model.entity.PaperEntry;
import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 马拉圈
* @description 针对表【paper_entry(题单-题目关联表)】的数据库操作Mapper
* @createDate 2024-05-14 23:32:01
* @Entity com.achobeta.domain.paper.model.entity.PaperEntry
*/
public interface PaperEntryMapper extends BaseMapper<PaperEntry> {

    PaperQuestionsVO getQuestionsOnPaper(@Param("paperId") Long paperId);

}




