package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnairePeriodMapper;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.entity.QuestionnairePeriod;
import com.achobeta.domain.recruitment.service.QuestionnairePeriodService;
import com.achobeta.domain.recruitment.service.TimePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【questionnaire_period(问卷-时间段关联表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionnairePeriodServiceImpl extends ServiceImpl<QuestionnairePeriodMapper, QuestionnairePeriod>
    implements QuestionnairePeriodService{

    private final TimePeriodService timePeriodService;

    private Long getQuestionnaireRecId(Long questionnaireId) {
        return Db.lambdaQuery(Questionnaire.class).eq(Questionnaire::getId, questionnaireId).oneOpt().orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTIONNAIRE_NOT_EXISTS)).getRecId();
    }

    @Override
    public Optional<QuestionnairePeriod> getQuestionnairePeriod(Long questionnaireId, Long periodId) {
        return this.lambdaQuery()
                .eq(QuestionnairePeriod::getQuestionnaireId, questionnaireId)
                .eq(QuestionnairePeriod::getPeriodId, periodId)
                .oneOpt();
    }

    private void addQuestionnairePeriod(Long questionnaireId, Long periodId) {
        getQuestionnairePeriod(questionnaireId, periodId)
                .ifPresentOrElse(questionnairePeriod -> {
                }, () -> {
                    // 插入一条新的
                    QuestionnairePeriod questionnairePeriod = new QuestionnairePeriod();
                    questionnairePeriod.setQuestionnaireId(questionnaireId);
                    questionnairePeriod.setPeriodId(periodId);
                    this.save(questionnairePeriod);
                });
    }

    private void removeQuestionnairePeriod(Long questionnaireId, Long periodId) {
        getQuestionnairePeriod(questionnaireId, periodId)
                .ifPresentOrElse(questionnairePeriod -> {
                    // 删除一条旧的
                    this.lambdaUpdate()
                            .eq(QuestionnairePeriod::getQuestionnaireId, questionnaireId)
                            .eq(QuestionnairePeriod::getPeriodId, periodId)
                            .remove();
                }, () -> {
                });
    }

    @Override
    public void putPeriods(Long questionnaireId, List<Long> periodIds) {
        Long recId = getQuestionnaireRecId(questionnaireId);
        Map<Long, Boolean> map = new HashMap<>();
        timePeriodService.getTimePeriods(recId).forEach(timePeriod -> {
            map.put(timePeriod.getId(), Boolean.FALSE);
        });
        // 将选中的时间段设置为 true（不存在于 map 的忽略即可）
        periodIds.stream().filter(map::containsKey).forEach(periodId -> {
            map.put(periodId, Boolean.TRUE);
        });
        // map 中 true 的尝试添加，false 的尝试删除
        map.forEach((periodId, value) -> {
            if (Boolean.TRUE.equals(value)) {
                addQuestionnairePeriod(questionnaireId, periodId);
            } else {
                removeQuestionnairePeriod(questionnaireId, periodId);
            }
        });
    }
}
