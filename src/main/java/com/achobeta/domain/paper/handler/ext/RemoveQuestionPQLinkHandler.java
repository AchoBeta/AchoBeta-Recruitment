package com.achobeta.domain.paper.handler.ext;

import com.achobeta.domain.paper.model.entity.PaperQuestionLink;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.question.handler.RemoveQuestionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 9:13
 */
@Component
@RequiredArgsConstructor
public class RemoveQuestionPQLinkHandler extends RemoveQuestionHandler {

    private final PaperQuestionLinkService paperQuestionLinkService;

    @Override
    public void handle(Long questionId) {
        // 删除关联表的一些行
        paperQuestionLinkService.lambdaUpdate()
                .eq(PaperQuestionLink::getQuestionId, questionId)
                .remove();
        super.doNextHandler(questionId);
    }
}
