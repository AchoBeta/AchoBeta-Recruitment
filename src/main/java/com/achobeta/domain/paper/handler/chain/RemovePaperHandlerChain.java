package com.achobeta.domain.paper.handler.chain;

import com.achobeta.domain.paper.handler.RemovePaperHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 8:45
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
    @Transactional
    public void handle(Long paperId) {
        log.info("责任链开始处理 [paperId 为 {}] 的“删除试卷”事件", paperId);
        super.doNextHandler(paperId);
        log.info("责任链处理完毕！");
    }
}