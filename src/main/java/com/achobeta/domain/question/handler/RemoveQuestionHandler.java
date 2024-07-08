package com.achobeta.domain.question.handler;

import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 8:44
 */
public abstract class RemoveQuestionHandler {

    private RemoveQuestionHandler next;

    public abstract void handle(Long questionId);

    public void setNextHandler(RemoveQuestionHandler handler) {
        this.next = handler;
    }

    protected void doNextHandler(Long questionId) {
        if(Objects.nonNull(this.next)) {
            this.next.handle(questionId);
        }
    }
}
