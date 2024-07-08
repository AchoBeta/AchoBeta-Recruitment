package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.entity.LibraryPaperLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【library_paper_link(试卷库-试卷关联表)】的数据库操作Service
* @createDate 2024-07-05 22:38:52
*/
public interface LibraryPaperLinkService extends IService<LibraryPaperLink> {

    void addLibraryPaperLinkBatch(List<Long> libIds, Long paperId);

    void addLibraryPaperLink(Long libId, Long paperId);

    void removeLibraryPaperLink(Long libId, Long paperId);
}
