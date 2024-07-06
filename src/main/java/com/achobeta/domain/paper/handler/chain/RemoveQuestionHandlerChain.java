package com.achobeta.domain.paper.handler.chain;

import com.achobeta.domain.paper.handler.RemoveQuestionHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 12:31
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
    public void handle(Long paperId, Long questionId) {
        log.info("责任链开始处理 [paperId 为 {}，questionId 为{}] 的“从试卷中移除一道题”事件", paperId, questionId);
        super.doNextHandler(paperId, questionId);
        log.info("责任链处理完毕！");
    }
}
