package com.achobeta.domain.paper.handler;

import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 8:41
 */
public abstract class RemovePaperHandler {

    private RemovePaperHandler next;

    public abstract void handle(Long paperId);

    public void setNextHandler(RemovePaperHandler handler) {
        this.next = handler;
    }

    protected void doNextHandler(Long paperId) {
        if(Objects.nonNull(this.next)) {
            this.next.handle(paperId);
        }
    }
}
