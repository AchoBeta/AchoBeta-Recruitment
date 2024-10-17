package com.achobeta.domain.paper.handler.ext;

import com.achobeta.domain.paper.handler.RemovePaperHandler;
import com.achobeta.domain.paper.model.entity.LibraryPaperLink;
import com.achobeta.domain.paper.model.entity.PaperQuestionLink;
import com.achobeta.domain.paper.service.LibraryPaperLinkService;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 11:47
 */
@Component
@RequiredArgsConstructor
public class RemovePaperLPLinkHandler extends RemovePaperHandler {

    private final LibraryPaperLinkService libraryPaperLinkService;

    private final PaperQuestionLinkService paperQuestionLinkService;

    @Override
    public void handle(Long paperId) {
        // 删除关联表的一些行
        libraryPaperLinkService.lambdaUpdate()
                .eq(LibraryPaperLink::getPaperId, paperId)
                .remove();
        paperQuestionLinkService.lambdaUpdate()
                .eq(PaperQuestionLink::getPaperId, paperId)
                .remove();
        super.doNextHandler(paperId);
    }

}
