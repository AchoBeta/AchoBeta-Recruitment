package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.entity.QuestionnairePeriod;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 马拉圈
* @description 针对表【questionnaire_period(问卷-时间段关联表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface QuestionnairePeriodService extends IService<QuestionnairePeriod> {

    QuestionnairePeriod getQuestionnairePeriod(Long questionnaireId, Long periodId);

    void addQuestionnairePeriod(Long questionnaireId, Long periodId);

    void removeQuestionnairePeriod(Long questionnaireId, Long periodId);

}
