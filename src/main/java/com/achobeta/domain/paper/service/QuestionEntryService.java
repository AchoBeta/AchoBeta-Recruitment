package com.achobeta.domain.paper.service;

import cn.hutool.core.lang.Opt;
import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.paper.model.entity.QuestionEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_entry(自定义问题表)】的数据库操作Service
* @createDate 2024-05-14 23:32:01
*/
public interface QuestionEntryService extends IService<QuestionEntry> {

    List<QuestionEntryVO> getQuestionEntries(Long libId);

    Optional<QuestionEntry> getQuestionEntry(Long questionId);

    Long addQuestionEntry(Long libId, String title);

    void renameQuestionTitle(Long questionId, String title);

    void removeQuestionEntry(Long questionId);

    void checkQuestionExists(Long questionId);

}
