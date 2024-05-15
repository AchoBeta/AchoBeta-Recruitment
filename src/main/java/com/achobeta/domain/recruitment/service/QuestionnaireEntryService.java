package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.dto.QuestionnaireEntryDTO;
import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【questionnaire_entry(问卷-自定义项关联表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface QuestionnaireEntryService extends IService<QuestionnaireEntry> {

    Optional<QuestionnaireEntry> getQuestionnaireEntry(Long questionnaireId, Long entryId);

    void putEntries(Long questionnaireId, List<QuestionnaireEntryDTO> questionnaireEntryDTOS);

}
