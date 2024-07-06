package com.achobeta.domain.qpaper.handler.ext;

import com.achobeta.domain.qpaper.handler.RemovePaperHandler;
import com.achobeta.domain.qpaper.model.entity.LibraryPaperLink;
import com.achobeta.domain.qpaper.service.LibraryPaperLinkService;
import com.achobeta.domain.question.model.entity.LibraryQuestionLink;
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

    @Override
    public void handle(Long paperId) {
        // 删除关联表的一些行
        libraryPaperLinkService.lambdaUpdate()
                .eq(LibraryPaperLink::getPaperId, paperId)
                .remove();
    }

}
