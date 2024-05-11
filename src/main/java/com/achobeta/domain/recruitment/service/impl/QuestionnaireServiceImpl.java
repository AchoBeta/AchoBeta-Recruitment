package com.achobeta.domain.recruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnaireMapper;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.vo.EntryVO;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireVO;
import com.achobeta.domain.recruitment.model.vo.TimePeriodVO;
import com.achobeta.domain.recruitment.service.CustomEntryService;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.achobeta.domain.recruitment.service.QuestionnaireService;
import com.achobeta.domain.recruitment.service.TimePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 马拉圈
* @description 针对表【questionnaire(问卷表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire>
    implements QuestionnaireService{

    private final static String DEFAULT_CONTENT = "";

    private final QuestionnaireMapper questionnaireMapper;

    private final QuestionnaireEntryService questionnaireEntryService;

    private final CustomEntryService customEntryService;

    private final TimePeriodService timePeriodService;

    @Override
    public QuestionnaireVO createQuestionnaire(Long stuId, Long recId) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setStuId(stuId);
        questionnaire.setRecId(recId);
        this.save(questionnaire);
        Long questionnaireId = questionnaire.getId();
        // 设置自定义项
        List<EntryVO> entryVOS = customEntryService
                .selectCustomEntry(recId)
                .stream().map(customEntry -> {
                    Long entryId = customEntry.getId();
                    questionnaireEntryService.addQuestionnaireEntry(questionnaireId, entryId, DEFAULT_CONTENT);
                    EntryVO entryVO = BeanUtil.copyProperties(customEntry, EntryVO.class);
                    entryVO.setContent(DEFAULT_CONTENT);
                    return entryVO;
                }).collect(Collectors.toList());
        QuestionnaireVO questionnaireVO = BeanUtil.copyProperties(questionnaire, QuestionnaireVO.class);
        questionnaireVO.setEntryVOS(entryVOS);
        return questionnaireVO;
    }



    @Override
    public QuestionnaireVO getQuestionnaire(Long stuId, Long recId) {
        return this.lambdaQuery()
                .eq(Questionnaire::getStuId, stuId)
                .eq(Questionnaire::getRecId, recId)
                .oneOpt()
                .map(questionnaire -> {
                    Long questionnaireId = questionnaire.getId();
                    // 获取自定义项
                    List<EntryVO> entryVOS = questionnaireMapper.getEntries(questionnaireId);
                    // 获取时间段
                    List<TimePeriodVO> timePeriodVOS = questionnaireMapper.getPeriods(questionnaireId);
                    QuestionnaireVO questionnaireVO = BeanUtil.copyProperties(questionnaire, QuestionnaireVO.class);
                    questionnaireVO.setEntryVOS(entryVOS);
                    questionnaireVO.setTimePeriodVOS(timePeriodVOS);
                    return questionnaireVO;
                }).orElseGet(() -> createQuestionnaire(stuId, recId));
    }

    @Override
    public void checkUser(Long stuId, Long questionnaireId) {
        Long userId = this.lambdaQuery()
                .eq(Questionnaire::getId, questionnaireId)
                .oneOpt().orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.QUESTIONNAIRE_NOT_EXISTS)
                ).getStuId();
        if(!userId.equals(stuId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
    }
}




