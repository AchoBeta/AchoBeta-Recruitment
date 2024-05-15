package com.achobeta.domain.recruitment.model.dao.mapper;

import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireEntryVO;
import com.achobeta.domain.recruitment.model.vo.TimePeriodVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【questionnaire(问卷表)】的数据库操作Mapper
* @createDate 2024-05-11 02:30:58
* @Entity com.achobeta.domain.recruitment.model.entity.Questionnaire
*/
public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {

    List<QuestionnaireEntryVO> getEntries(@Param("questionnaireId") Long questionnaireId);

    List<TimePeriodVO> getPeriods(@Param("questionnaireId") Long questionnaireId);

}




