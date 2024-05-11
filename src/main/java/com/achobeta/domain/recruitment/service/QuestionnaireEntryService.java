package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.achobeta.domain.recruitment.model.vo.EntryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【questionnaire_entry(问卷-自定义项关联表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface QuestionnaireEntryService extends IService<QuestionnaireEntry> {

    void addQuestionnaireEntry(Long questionnaireId, Long entryId, String content);

    void updateQuestionnaireEntry(Long questionnaireId, Long entryId, String content);

}
