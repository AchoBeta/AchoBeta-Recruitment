package com.achobeta.domain.recruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.service.RecruitmentActivityService;
import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnaireMapper;
import com.achobeta.domain.recruitment.model.dto.QuestionnaireDTO;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireEntryVO;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireVO;
import com.achobeta.domain.recruitment.model.vo.TimePeriodVO;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.achobeta.domain.recruitment.service.QuestionnairePeriodService;
import com.achobeta.domain.recruitment.service.QuestionnaireService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    private final RecruitmentActivityService recruitmentActivityService;

    private final QuestionnaireMapper questionnaireMapper;

    private final QuestionnaireEntryService questionnaireEntryService;

    private final QuestionnairePeriodService questionnairePeriodService;

    @Override
    public Optional<Questionnaire> getQuestionnaire(Long questionnaireId) {
        return this.lambdaQuery()
                .eq(Questionnaire::getId, questionnaireId)
                .oneOpt();
    }

    @Override
    public QuestionnaireVO createQuestionnaire(Long stuId, Long recId) {
        // 判断招新活动是否开始
        recruitmentActivityService.checkActivityRun(recId);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setStuId(stuId);
        questionnaire.setRecId(recId);
        this.save(questionnaire);
        QuestionnaireVO questionnaireVO = BeanUtil.copyProperties(questionnaire, QuestionnaireVO.class);
        questionnaireVO.setQuestionnaireEntryVOS(new ArrayList<>());
        questionnaireVO.setTimePeriodVOS(new ArrayList<>());
        return questionnaireVO;
    }

    @Override
    public QuestionnaireVO getQuestionnaire(Long stuId, Long recId) {
        // 如果有问卷，那无论有没有启动，都可以获得问卷，但是如果之前没填写过，则不会生成一份新的
        return this.lambdaQuery()
                .eq(Questionnaire::getStuId, stuId)
                .eq(Questionnaire::getRecId, recId)
                .oneOpt()
                .map(questionnaire -> {
                    Long questionnaireId = questionnaire.getId();
                    // 转化
                    QuestionnaireVO questionnaireVO = BeanUtil.copyProperties(questionnaire, QuestionnaireVO.class);
                    // 获取自定义项
                    List<QuestionnaireEntryVO> questionnaireEntryVOS = questionnaireMapper.getEntries(questionnaireId);
                    questionnaireVO.setQuestionnaireEntryVOS(questionnaireEntryVOS);
                    // 获取时间段
                    List<TimePeriodVO> timePeriodVOS = questionnaireMapper.getPeriods(questionnaireId);
                    questionnaireVO.setTimePeriodVOS(timePeriodVOS);
                    return questionnaireVO;
                }).orElseGet(() -> createQuestionnaire(stuId, recId));
    }

    @Override
    public void submitQuestionnaire(QuestionnaireDTO questionnaireDTO) {
        Long questionnaireId = questionnaireDTO.getQuestionnaireId();
        // 更新自定义项
        questionnaireEntryService.putEntries(questionnaireId, questionnaireDTO.getQuestionnaireEntryDTOS());
        // 更新时间段
        questionnairePeriodService.putPeriods(questionnaireId, questionnaireDTO.getPeriodIds());
    }

    @Override
    public void checkUser(Long stuId, Long questionnaireId) {
        Long userId = getQuestionnaire(questionnaireId)
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.QUESTIONNAIRE_NOT_EXISTS)
                ).getStuId();
        if(!userId.equals(stuId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
    }

    @Override
    public Long getQuestionnaireRecId(Long questionnaireId) {
        return getQuestionnaire(questionnaireId)
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.QUESTIONNAIRE_NOT_EXISTS)
                ).getRecId();
    }

    @Override
    public List<Long> getQuestionnaireIdsByPaperId(Long paperId) {
        return recruitmentActivityService.getRecIdsByPaperId(paperId)
                .stream()
                .parallel()
                .map(recruitmentActivityService::getQuestionnaireIds)
                .flatMap(Collection::stream)
                .toList();
    }

}
