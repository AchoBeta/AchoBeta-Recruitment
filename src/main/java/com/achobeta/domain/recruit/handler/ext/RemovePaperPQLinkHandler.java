package com.achobeta.domain.recruit.handler.ext;

import com.achobeta.domain.paper.handler.RemovePaperHandler;
import com.achobeta.domain.recruit.model.entity.ParticipationQuestionLink;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.ParticipationQuestionLinkService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 18:01
 */
@Component
@RequiredArgsConstructor
public class RemovePaperPQLinkHandler extends RemovePaperHandler {

    private final RecruitmentActivityService recruitmentActivityService;

    private final ActivityParticipationService activityParticipationService;

    private final ParticipationQuestionLinkService participationQuestionLinkService;

    @Override
    public void handle(Long paperId) {
        List<Long> participationIds = activityParticipationService.getParticipationIdsByPaperId(paperId);
        if(!CollectionUtils.isEmpty(participationIds)) {
            // 删除对应的行
            participationQuestionLinkService.lambdaUpdate()
                    .in(ParticipationQuestionLink::getParticipationId, participationIds)
                    .remove();
        }
        // 涉及的招新活动的 paperId 置为 null
        List<Long> actIds = recruitmentActivityService.getActIdsByPaperId(paperId);
        if(!CollectionUtils.isEmpty(actIds)) {
            recruitmentActivityService.lambdaUpdate()
                    .in(RecruitmentActivity::getId, actIds)
                    .set(RecruitmentActivity::getPaperId, null)
                    .update();
        }
        // 执行下一个
        super.doNextHandler(paperId);
    }
}
