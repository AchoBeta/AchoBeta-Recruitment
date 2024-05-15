package com.achobeta.domain.paper.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.paper.model.entity.QuestionEntry;
import com.achobeta.domain.paper.service.QuestionEntryService;
import com.achobeta.domain.paper.model.dao.mapper.QuestionEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_entry(自定义问题表)】的数据库操作Service实现
* @createDate 2024-05-14 23:32:01
*/
@Service
@RequiredArgsConstructor
public class QuestionEntryServiceImpl extends ServiceImpl<QuestionEntryMapper, QuestionEntry>
    implements QuestionEntryService{

    private final QuestionEntryMapper questionEntryMapper;

    @Override
    public List<QuestionEntryVO> getQuestionEntries(Long libId) {
        return questionEntryMapper.getQuestionEntries(libId);
    }

    @Override
    public Optional<QuestionEntry> getQuestionEntry(Long questionId) {
        return this.lambdaQuery()
                .eq(QuestionEntry::getId, questionId)
                .oneOpt();
    }

    @Override
    public Long addQuestionEntry(Long libId, String title) {
        QuestionEntry questionEntry = new QuestionEntry();
        questionEntry.setLibId(libId);
        questionEntry.setTitle(title);
        this.save(questionEntry);
        return questionEntry.getId();
    }

    @Override
    public void renameQuestionTitle(Long questionId, String title) {
        this.lambdaUpdate()
                .eq(QuestionEntry::getId, questionId)
                .set(QuestionEntry::getTitle, title)
                .update();
    }

    @Override
    public void removeQuestionEntry(Long questionId) {
        this.lambdaUpdate()
                .eq(QuestionEntry::getId, questionId)
                .remove();
    }

    @Override
    public void checkQuestionExists(Long questionId) {
        getQuestionEntry(questionId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_NOT_EXISTS));
    }
}




