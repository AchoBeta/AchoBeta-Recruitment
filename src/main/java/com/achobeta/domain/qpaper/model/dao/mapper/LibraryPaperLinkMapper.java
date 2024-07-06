package com.achobeta.domain.qpaper.model.dao.mapper;

import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.achobeta.domain.qpaper.model.entity.LibraryPaperLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 马拉圈
* @description 针对表【library_paper_link(试卷库-试卷关联表)】的数据库操作Mapper
* @createDate 2024-07-05 22:38:52
* @Entity com.achobeta.domain.qpaper.model.entity.LibraryPaperLink
*/
public interface LibraryPaperLinkMapper extends BaseMapper<LibraryPaperLink> {

    PaperQuestionsVO getPaperDetail(@Param("paperId") Long paperId);

}




