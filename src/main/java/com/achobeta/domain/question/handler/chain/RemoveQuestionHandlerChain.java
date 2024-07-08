package com.achobeta.domain.question.handler.chain;

import com.achobeta.domain.question.handler.RemoveQuestionHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 8:46
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RemoveQuestionHandlerChain extends RemoveQuestionHandler {

    private final List<RemoveQuestionHandler> removeQuestionHandlers;

    private RemoveQuestionHandler initHandlerChain() {
        int size = removeQuestionHandlers.size();
        if(size == 0) {
            return null;
        }
        for (int i = 0; i < size - 1; i++) {
            removeQuestionHandlers.get(i).setNextHandler(removeQuestionHandlers.get(i + 1));
        }
        return removeQuestionHandlers.getFirst();
    }

    @PostConstruct
    public void doPostConstruct() {
        this.setNextHandler(initHandlerChain());
    }

    @Override
    public void handle(Long questionId) {
        log.info("责任链开始处理 [questionId 为 {}] 的“删除问题”事件", questionId);
        super.doNextHandler(questionId);
        log.info("责任链处理完毕！");
    }
}
