package com.achobeta.domain.paper.handler;

import com.achobeta.domain.paper.model.dto.PaperEntryDTO;

import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 12:27
 */
public abstract class RemoveQuestionHandler {

    private RemoveQuestionHandler next;

    public abstract void handle(Long paperId, Long questionId);

    public void setNextHandler(RemoveQuestionHandler handler) {
        this.next = handler;
    }

    protected void doNextHandler(Long paperId, Long questionId) {
        if(Objects.nonNull(this.next)) {
            this.next.handle(paperId, questionId);
        }
    }
}
