package com.achobeta.domain.paper.model.dao.mapper;

import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.paper.model.entity.QuestionEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【question_entry(自定义问题表)】的数据库操作Mapper
* @createDate 2024-05-14 23:32:01
* @Entity com.achobeta.domain.paper.model.entity.QuestionEntry
*/
public interface QuestionEntryMapper extends BaseMapper<QuestionEntry> {

    List<QuestionEntryVO> getQuestionEntries(@Param("libId") Long libId);

}




