package com.achobeta.domain.recruitment.model.dao.mapper;

import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.achobeta.domain.recruitment.model.vo.EntryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【questionnaire_entry(问卷-自定义项关联表)】的数据库操作Mapper
* @createDate 2024-05-11 02:30:58
* @Entity com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry
*/
public interface QuestionnaireEntryMapper extends BaseMapper<QuestionnaireEntry> {

}




