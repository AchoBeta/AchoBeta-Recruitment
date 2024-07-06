package com.achobeta.domain.question.handler.ext;

import com.achobeta.domain.question.handler.RemoveQuestionHandler;
import com.achobeta.domain.question.model.entity.LibraryQuestionLink;
import com.achobeta.domain.question.service.LibraryQuestionLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 9:11
 */
@Component
@RequiredArgsConstructor
public class RemoveQuestionLQLinkHandler extends RemoveQuestionHandler {

    private final LibraryQuestionLinkService libraryQuestionLinkService;

    @Override
    public void handle(Long questionId) {
        // 删除关联表的一些行
        libraryQuestionLinkService.lambdaUpdate()
                .eq(LibraryQuestionLink::getQuestionId, questionId)
                .remove();
    }
}
