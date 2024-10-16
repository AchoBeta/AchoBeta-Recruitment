package com.achobeta.domain.recruit.handler.ext;

import com.achobeta.domain.paper.handler.RemoveQuestionFromPaperHandler;
import com.achobeta.domain.recruit.model.entity.ParticipationQuestionLink;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.ParticipationQuestionLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 17:25
 */
@Component
@RequiredArgsConstructor
public class RemoveQuestionFromPaperPQLinkHandler extends RemoveQuestionFromPaperHandler {

    private final ActivityParticipationService activityParticipationService;

    private final ParticipationQuestionLinkService participationQuestionLinkService;

    @Override
    public void handle(Long paperId, List<Long> questionIds) {
        List<Long> participationIds = activityParticipationService.getParticipationIdsByPaperId(paperId);
        if(!CollectionUtils.isEmpty(participationIds)) {
            // 删除对应的行
            participationQuestionLinkService.lambdaUpdate()
                    .in(ParticipationQuestionLink::getParticipationId, participationIds)
                    .in(ParticipationQuestionLink::getQuestionId, questionIds)
                    .remove();
        }
        // 执行下一个
        super.doNextHandler(paperId, questionIds);
    }

}
