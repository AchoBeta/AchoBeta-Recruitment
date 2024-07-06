package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnaireEntryMapper;
import com.achobeta.domain.recruitment.model.dto.QuestionnaireEntryDTO;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.achobeta.domain.recruitment.service.RecruitmentActivityService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final static String DEFAULT_ANSWER = "";

    private final RecruitmentActivityService recruitmentActivityService;

    private Long getQuestionnaireRecId(Long questionnaireId) {
        return Db.lambdaQuery(Questionnaire.class).eq(Questionnaire::getId, questionnaireId).oneOpt().orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTIONNAIRE_NOT_EXISTS)).getRecId();
    }

    @Override
    public Optional<QuestionnaireEntry> getQuestionnaireEntry(Long questionnaireId, Long entryId) {
        return this.lambdaQuery()
                .eq(QuestionnaireEntry::getQuestionnaireId, questionnaireId)
                .eq(QuestionnaireEntry::getEntryId, entryId)
                .oneOpt();
    }

    private void addQuestionnaireEntry(Long questionnaireId, Long entryId, String answer) {
        QuestionnaireEntry questionnaireEntry = new QuestionnaireEntry();
        questionnaireEntry.setQuestionnaireId(questionnaireId);
        questionnaireEntry.setEntryId(entryId);
        questionnaireEntry.setAnswer(answer);
        this.save(questionnaireEntry);
    }

    private void updateQuestionnaireEntry(Long questionnaireId, Long entryId, String answer) {
        this.lambdaUpdate()
                .eq(QuestionnaireEntry::getQuestionnaireId, questionnaireId)
                .eq(QuestionnaireEntry::getEntryId, entryId)
                .set(QuestionnaireEntry::getAnswer, answer)
                .update();
    }

    private void addOrUpdateQuestionnaireEntry(Long questionnaireId, Long entryId, String answer) {
        getQuestionnaireEntry(questionnaireId, entryId)
                .ifPresentOrElse(questionnaireEntry -> {
                    updateQuestionnaireEntry(questionnaireId, entryId, answer);
                }, () -> {
                    addQuestionnaireEntry(questionnaireId, entryId, answer);
                });
    }

    @Override
    public void putEntries(Long questionnaireId, List<QuestionnaireEntryDTO> questionnaireEntryDTOS) {
        Long recId = getQuestionnaireRecId(questionnaireId);
        Map<Long, String> map = new HashMap<>();
        // 获取答题模板
        recruitmentActivityService.getPaperQuestions(recId).forEach(questionEntry -> {
            map.put(questionEntry.getId(), DEFAULT_ANSWER);
        });
        // 将答案填入模板
        questionnaireEntryDTOS.stream()
                .filter(questionnaireEntryDTO -> map.containsKey(questionnaireEntryDTO.getEntryId()))
                .filter(questionnaireEntryDTO -> StringUtils.hasText(questionnaireEntryDTO.getAnswer()))
                .forEach(questionnaireEntryDTO -> {
                    map.put(questionnaireEntryDTO.getEntryId(), questionnaireEntryDTO.getAnswer());
                });
        map.forEach((entryId, answer) -> {
            addOrUpdateQuestionnaireEntry(questionnaireId, entryId, answer);
        });
    }

}
