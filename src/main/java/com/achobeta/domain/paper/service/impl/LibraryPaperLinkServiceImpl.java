package com.achobeta.domain.paper.service.impl;

import com.achobeta.domain.paper.model.dao.mapper.LibraryPaperLinkMapper;
import com.achobeta.domain.paper.model.entity.LibraryPaperLink;
import com.achobeta.domain.paper.service.LibraryPaperLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【library_paper_link(试卷库-试卷关联表)】的数据库操作Service实现
* @createDate 2024-07-05 22:38:52
*/
@Service
public class LibraryPaperLinkServiceImpl extends ServiceImpl<LibraryPaperLinkMapper, LibraryPaperLink>
    implements LibraryPaperLinkService{

    @Override
    @Transactional
    public void addLibraryPaperLinkBatch(List<Long> libIds, Long paperId) {
        List<LibraryPaperLink> libraryPaperLinks = libIds.stream().map(libId -> {
            LibraryPaperLink libraryPaperLink = new LibraryPaperLink();
            libraryPaperLink.setLibId(libId);
            libraryPaperLink.setPaperId(paperId);
            return libraryPaperLink;
        }).toList();
        this.saveBatch(libraryPaperLinks);
    }

    @Override
    public void addLibraryPaperLink(Long libId, Long paperId) {
        LibraryPaperLink libraryPaperLink = new LibraryPaperLink();
        libraryPaperLink.setLibId(libId);
        libraryPaperLink.setPaperId(paperId);
        this.save(libraryPaperLink);
    }

    @Override
    public void removeLibraryPaperLink(Long libId, Long paperId) {
        this.lambdaUpdate()
                .eq(LibraryPaperLink::getLibId, libId)
                .eq(LibraryPaperLink::getPaperId, paperId)
                .remove();
    }
}




