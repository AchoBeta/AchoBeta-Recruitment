package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnaireEntryMapper;
import com.achobeta.domain.recruitment.model.dto.EntryDTO;
import com.achobeta.domain.recruitment.model.entity.*;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public QuestionnaireEntry getQuestionnaireEntry(Long questionnaireId, Long entryId) {
        return this.lambdaQuery()
                .eq(QuestionnaireEntry::getQuestionnaireId, questionnaireId)
                .eq(QuestionnaireEntry::getEntryId, entryId)
                .one();
    }

    @Override
    public void checkQuestionnaireEntryId(Long questionnaireId, Long entryId) {
        Long recId1 = Db.lambdaQuery(Questionnaire.class).eq(Questionnaire::getId, questionnaireId).oneOpt().orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTIONNAIRE_NOT_EXISTS)).getRecId();
        Long recId2 = Db.lambdaQuery(CustomEntry.class).eq(CustomEntry::getId, entryId).oneOpt().orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.ENTRY_NOT_EXISTS)).getRecId();
        if(!recId1.equals(recId2)) {
            throw new GlobalServiceException(String.format("数据不一致，%d 与 %d 对应不上", questionnaireId, entryId),
                    GlobalServiceStatusCode.PARAM_NOT_VALID);
        }
    }

    @Override
    public void addOrUpdateQuestionnaireEntry(Long questionnaireId, Long entryId, String content) {
        // 判断招新 id 是否一致
        checkQuestionnaireEntryId(questionnaireId, entryId);
        Optional.ofNullable(getQuestionnaireEntry(questionnaireId, entryId))
                .ifPresentOrElse(questionnaireEntry -> {
                    this.lambdaUpdate()
                            .eq(QuestionnaireEntry::getQuestionnaireId, questionnaireId)
                            .eq(QuestionnaireEntry::getEntryId, entryId)
                            .set(QuestionnaireEntry::getContent, content)
                            .update();
                }, () -> {
                    QuestionnaireEntry questionnaireEntry = new QuestionnaireEntry();
                    questionnaireEntry.setQuestionnaireId(questionnaireId);
                    questionnaireEntry.setEntryId(entryId);
                    questionnaireEntry.setContent(content);
                    this.save(questionnaireEntry);
                });
    }

    @Override
    public void putEntries(Long questionnaireId, List<EntryDTO> entryDTOS) {
        entryDTOS.stream().parallel().forEach(entryDTO -> {
            Long entryId = entryDTO.getEntryId();
            String content = entryDTO.getContent();
            addOrUpdateQuestionnaireEntry(questionnaireId, entryId, content);
        });
    }

}




