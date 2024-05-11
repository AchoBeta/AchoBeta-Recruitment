package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnaireEntryMapper;
import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author 马拉圈
* @description 针对表【questionnaire_entry(问卷-自定义项关联表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionnaireEntryServiceImpl extends ServiceImpl<QuestionnaireEntryMapper, QuestionnaireEntry>
    implements QuestionnaireEntryService{

    @Override
    public void addQuestionnaireEntry(Long questionnaireId, Long entryId, String content) {
        QuestionnaireEntry questionnaireEntry = new QuestionnaireEntry();
        questionnaireEntry.setQuestionnaireId(questionnaireId);
        questionnaireEntry.setEntryId(entryId);
        questionnaireEntry.setContent(content);
        this.save(questionnaireEntry);
    }

    @Override
    public void updateQuestionnaireEntry(Long questionnaireId, Long entryId, String content) {
        this.lambdaUpdate()
                .eq(QuestionnaireEntry::getQuestionnaireId, questionnaireId)
                .eq(QuestionnaireEntry::getEntryId, entryId)
                .set(QuestionnaireEntry::getContent, content)
                .update();
    }

}




