package com.achobeta.domain.qpaper.handler;

import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 8:44
 */
public abstract class RemoveQuestionFromPaperHandler {

    private RemoveQuestionFromPaperHandler next;

    public abstract void handle(Long paperId, List<Long> questionIds);

    public void setNextHandler(RemoveQuestionFromPaperHandler handler) {
        this.next = handler;
    }

    protected void doNextHandler(Long paperId, List<Long> questionIds) {
        if(Objects.nonNull(this.next)) {
            this.next.handle(paperId, questionIds);
        }
    }
}
