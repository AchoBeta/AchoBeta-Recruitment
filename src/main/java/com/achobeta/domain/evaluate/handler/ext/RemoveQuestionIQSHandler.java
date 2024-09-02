package com.achobeta.domain.evaluate.handler.ext;

import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.evaluate.service.InterviewQuestionScoreService;
import com.achobeta.domain.question.handler.RemoveQuestionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 3:55
 */
@Component
@RequiredArgsConstructor
public class RemoveQuestionIQSHandler extends RemoveQuestionHandler {

    private final InterviewQuestionScoreService interviewQuestionScoreService;

    @Override
    public void handle(Long questionId) {
        // 删除这道题相关的所有评分
        interviewQuestionScoreService.lambdaUpdate()
                .eq(InterviewQuestionScore::getQuestionId, questionId)
                .remove();
        // 执行下一个
        super.doNextHandler(questionId);
    }
}
