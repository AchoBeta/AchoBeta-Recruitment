package com.achobeta.domain.question.service.impl;

import com.achobeta.domain.question.model.dao.mapper.LibraryQuestionLinkMapper;
import com.achobeta.domain.question.model.entity.LibraryQuestionLink;
import com.achobeta.domain.question.service.LibraryQuestionLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【library_question_link(题库-问题关联表)】的数据库操作Service实现
* @createDate 2024-07-05 20:03:35
*/
@Service
public class LibraryQuestionLinkServiceImpl extends ServiceImpl<LibraryQuestionLinkMapper, LibraryQuestionLink>
    implements LibraryQuestionLinkService{

    @Override
    public void addLibraryQuestionLinkBatch(List<Long> libIds, Long questionId) {
        List<LibraryQuestionLink> libraryQuestionLinks = libIds.stream().map(libId -> {
            LibraryQuestionLink libraryQuestionLink = new LibraryQuestionLink();
            libraryQuestionLink.setLibId(libId);
            libraryQuestionLink.setQuestionId(questionId);
            return libraryQuestionLink;
        }).toList();
        this.saveBatch(libraryQuestionLinks);
    }

    @Override
    public void addLibraryQuestionLink(Long libId, Long questionId) {
        LibraryQuestionLink libraryQuestionLink = new LibraryQuestionLink();
        libraryQuestionLink.setLibId(libId);
        libraryQuestionLink.setQuestionId(questionId);
        this.save(libraryQuestionLink);
    }

    @Override
    public void removeLibraryQuestionLink(Long libId, Long questionId) {
        this.lambdaUpdate()
                .eq(LibraryQuestionLink::getLibId, libId)
                .eq(LibraryQuestionLink::getQuestionId, questionId)
                .remove();
    }
}




