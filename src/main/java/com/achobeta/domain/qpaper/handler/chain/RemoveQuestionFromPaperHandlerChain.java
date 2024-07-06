package com.achobeta.domain.qpaper.handler.chain;

import com.achobeta.domain.paper.handler.RemoveQuestionHandler;
import com.achobeta.domain.qpaper.handler.RemoveQuestionFromPaperHandler;
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
public class RemoveQuestionFromPaperHandlerChain extends RemoveQuestionFromPaperHandler {

    private final List<RemoveQuestionFromPaperHandler> removeQuestionFromPaperHandlers;

    private RemoveQuestionFromPaperHandler initHandlerChain() {
        int size = removeQuestionFromPaperHandlers.size();
        if(size == 0) {
            return null;
        }
        for (int i = 0; i < size - 1; i++) {
            removeQuestionFromPaperHandlers.get(i).setNextHandler(removeQuestionFromPaperHandlers.get(i + 1));
        }
        return removeQuestionFromPaperHandlers.getFirst();
    }

    @PostConstruct
    public void doPostConstruct() {
        this.setNextHandler(initHandlerChain());
    }

    @Override
    public void handle(Long paperId, List<Long> questionIds) {
        log.info("责任链开始处理 [paperId 为 {}，questionIds 为{}] 的“从试卷中移除若干题”事件", paperId, questionIds);
        super.doNextHandler(paperId, questionIds);
        log.info("责任链处理完毕！");
    }
}
