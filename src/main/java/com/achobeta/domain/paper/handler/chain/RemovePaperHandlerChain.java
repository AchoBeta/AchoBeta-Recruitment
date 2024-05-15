package com.achobeta.domain.paper.handler.chain;

import com.achobeta.domain.paper.handler.RemovePaperHandler;
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
 * Time: 13:04
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RemovePaperHandlerChain extends RemovePaperHandler {

    private final List<RemovePaperHandler> removePaperHandlers;

    private RemovePaperHandler initHandlerChain() {
        int size = removePaperHandlers.size();
        if(size == 0) {
            return null;
        }
        for (int i = 0; i < size - 1; i++) {
            removePaperHandlers.get(i).setNextHandler(removePaperHandlers.get(i + 1));
        }
        return removePaperHandlers.getFirst();
    }

    @PostConstruct
    public void doPostConstruct() {
        this.setNextHandler(initHandlerChain());
    }

    @Override
    public void handle(Long paperId) {
        log.warn("责任链开始处理 paperId: {}", paperId);
        super.doNextHandler(paperId);
        log.warn("责任链处理完毕！");
    }
}
