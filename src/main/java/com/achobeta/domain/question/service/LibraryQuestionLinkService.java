package com.achobeta.domain.question.service;

import com.achobeta.domain.question.model.entity.LibraryQuestionLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【library_question_link(题库-问题关联表)】的数据库操作Service
* @createDate 2024-07-05 20:03:35
*/
public interface LibraryQuestionLinkService extends IService<LibraryQuestionLink> {

    void addLibraryQuestionLinkBatch(List<Long> libIds, Long questionId);

    void addLibraryQuestionLink(Long libId, Long questionId);

    void removeLibraryQuestionLink(Long libId, Long questionId);

}
