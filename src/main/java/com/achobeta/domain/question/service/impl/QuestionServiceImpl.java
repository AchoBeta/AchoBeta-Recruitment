package com.achobeta.domain.question.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.question.model.dao.mapper.QuestionLibraryMapper;
import com.achobeta.domain.question.model.dao.mapper.QuestionMapper;
import com.achobeta.domain.question.model.entity.LibraryQuestionLink;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.question.service.LibraryQuestionLinkService;
import com.achobeta.domain.question.service.QuestionService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question(问题表)】的数据库操作Service实现
* @createDate 2024-07-05 20:03:35
*/
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{


    private final QuestionMapper questionMapper;

    private final QuestionLibraryMapper questionLibraryMapper;

    private final LibraryQuestionLinkService libraryQuestionLinkService;

    @Override
    public List<QuestionVO> getQuestionsByLibId(Long libId) {
        return questionMapper.getQuestionsByLibId(libId);
    }

    @Override
    public Optional<Question> getQuestion(Long questionId) {
        return this.lambdaQuery()
                .eq(Question::getId, questionId)
                .oneOpt();
    }

    @Override
    public void addQuestion(List<Long> libIds, String title, String standard) {
        Question question = new Question();
        question.setTitle(title);
        question.setStandard(standard);
        this.save(question);
        Long questionId = question.getId();
        libraryQuestionLinkService.addLibraryQuestionLinkBatch(libIds, questionId);
    }

    @Override
    public void updateQuestion(Long questionId, List<Long> libIds, String title, String standard) {
        this.lambdaUpdate()
                .eq(Question::getId, questionId)
                .set(Question::getTitle, title)
                .set(Question::getStandard, standard)
                .update();
        // 获取这个问题所在的题库（旧）
        Map<Long, Boolean> hash = new HashMap<>();
        libraryQuestionLinkService.lambdaQuery()
                .eq(LibraryQuestionLink::getQuestionId, questionId)
                .list()
                .forEach(link -> hash.put(link.getLibId(), Boolean.FALSE));
        // 这样之后，新增的就是 true，需要删除的就是 false，已存在的则不出现在 hash 里
        libIds.forEach(libId -> {
            if (hash.containsKey(libId)) {
                hash.remove(libId);
            } else {
                hash.put(libId, Boolean.TRUE);
            }
        });
        // 遍历 hash 进行新增/删除
        hash.forEach((libId, flag) -> {
            if (Boolean.TRUE.equals(flag)) {
                libraryQuestionLinkService.addLibraryQuestionLink(libId, questionId);
            } else {
                libraryQuestionLinkService.removeLibraryQuestionLink(libId, questionId);
            }
        });

    }

    @Override
    public void removeQuestion(Long questionId) {
        this.lambdaUpdate()
                .eq(Question::getId, questionId)
                .remove();
    }

    @Override
    public void checkQuestionExists(Long questionId) {
        getQuestion(questionId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_NOT_EXISTS));
    }

    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        return questionLibraryMapper.getQuestionDetail(questionId);
    }

}




