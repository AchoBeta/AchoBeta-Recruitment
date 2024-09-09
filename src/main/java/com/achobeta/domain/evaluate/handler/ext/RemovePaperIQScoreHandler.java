package com.achobeta.domain.evaluate.handler.ext;

import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.evaluate.service.InterviewQuestionScoreService;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.handler.RemovePaperHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 3:17
 */
@Component
@RequiredArgsConstructor
public class RemovePaperIQScoreHandler extends RemovePaperHandler {

    private final InterviewService interviewService;

    private final InterviewQuestionScoreService interviewQuestionScoreService;

    @Override
    public void handle(Long paperId) {
        List<Long> interviewIds = interviewService.lambdaQuery()
                .eq(Interview::getPaperId, paperId)
                .list()
                .stream()
                .map(Interview::getId)
                .toList();
        if(CollectionUtils.isEmpty(interviewIds)) {
            return;
        }
        // 将面试试卷设置为空
        interviewService.lambdaUpdate()
                .in(Interview::getId, interviewIds)
                .set(Interview::getPaperId, null)
                .update();
        // 删除对应的评分
        interviewQuestionScoreService.lambdaUpdate()
                .in(InterviewQuestionScore::getInterviewId, interviewIds)
                .remove();
        // 执行下一个
        super.doNextHandler(paperId);
    }
}
