package com.achobeta.domain.recruitment.handler.ext;

import cn.hutool.core.collection.CollectionUtil;
import com.achobeta.domain.paper.handler.RemovePaperHandler;
import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.achobeta.domain.recruitment.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruitment.service.QuestionnaireService;
import com.achobeta.domain.recruitment.service.RecruitmentActivityService;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 13:08
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuestionnairePaperHandler extends RemovePaperHandler {

    private final RecruitmentActivityService recruitmentActivityService;

    private final QuestionnaireService questionnaireService;

    @Override
    public void handle(Long paperId) {
        // 找到所有以 paperId 为问卷模板的招新活动的问卷 id 集合
        List<Long> questionnaireIds = questionnaireService.getQuestionnaireIdsByPaperId(paperId);
        if(CollectionUtil.isEmpty(questionnaireIds)) {
            return;
        }
        // 删除对应的一行
        Db.lambdaUpdate(QuestionnaireEntry.class)
                .in(QuestionnaireEntry::getQuestionnaireId, questionnaireIds)
                .remove();
        // 将涉及的招新活动的 paperId 置为 null
        List<Long> recIds = recruitmentActivityService.getRecIdsByPaperId(paperId);
        if(CollectionUtil.isEmpty(recIds)) {
            return;
        }
        Db.lambdaUpdate(RecruitmentActivity.class)
                .in(RecruitmentActivity::getId, recIds)
                .set(RecruitmentActivity::getPaperId, null)
                .update();
        // 执行下一个
        super.doNextHandler(paperId);
    }

}
