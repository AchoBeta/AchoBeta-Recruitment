package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.QuestionnairePeriodMapper;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.entity.QuestionnairePeriod;
import com.achobeta.domain.recruitment.model.entity.TimePeriod;
import com.achobeta.domain.recruitment.service.QuestionnairePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【questionnaire_period(问卷-时间段关联表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
public class QuestionnairePeriodServiceImpl extends ServiceImpl<QuestionnairePeriodMapper, QuestionnairePeriod>
    implements QuestionnairePeriodService{

    @Override
    public QuestionnairePeriod getQuestionnairePeriod(Long questionnaireId, Long periodId) {
        return this.lambdaQuery()
                .eq(QuestionnairePeriod::getQuestionnaireId, questionnaireId)
                .eq(QuestionnairePeriod::getPeriodId, periodId)
                .one();
    }

    @Override
    public void addQuestionnairePeriod(Long questionnaireId, Long periodId) {
        Optional.ofNullable(getQuestionnairePeriod(questionnaireId, periodId)).ifPresent(questionnairePeriod -> {
            throw new GlobalServiceException(GlobalServiceStatusCode.PERIOD_IS_SELECTED);
        });
        // 判断招新 id 是否一致
        Long recId1 = Db.lambdaQuery(Questionnaire.class).eq(Questionnaire::getId, questionnaireId).oneOpt().orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.PERIOD_NOT_EXISTS)).getRecId();
        Long recId2 = Db.lambdaQuery(TimePeriod.class).eq(TimePeriod::getId, periodId).oneOpt().orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.PERIOD_NOT_EXISTS)).getRecId();
        if(!recId1.equals(recId2)) {
            throw new GlobalServiceException(String.format("数据不一致，%d 与 %d 对应不上", questionnaireId, periodId),
                    GlobalServiceStatusCode.PARAM_NOT_VALID);
        }
        QuestionnairePeriod questionnairePeriod = new QuestionnairePeriod();
        questionnairePeriod.setQuestionnaireId(questionnaireId);
        questionnairePeriod.setPeriodId(periodId);
        this.save(questionnairePeriod);
    }

    @Override
    public void removeQuestionnairePeriod(Long questionnaireId, Long periodId) {
        Optional.ofNullable(getQuestionnairePeriod(questionnaireId, periodId)).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.PERIOD_IS_NOT_SELECTED)
        );
        this.lambdaUpdate()
                .eq(QuestionnairePeriod::getQuestionnaireId, questionnaireId)
                .eq(QuestionnairePeriod::getPeriodId, periodId)
                .remove();
    }
}




