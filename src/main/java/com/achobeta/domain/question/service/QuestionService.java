package com.achobeta.domain.question.service;

import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question(问题表)】的数据库操作Service
* @createDate 2024-07-05 20:03:35
*/
public interface QuestionService extends IService<Question> {

    List<QuestionVO> getQuestionsByLibId(Long libId);

    Optional<Question> getQuestion(Long questionId);

    @Transactional
    void addQuestion(List<Long> libIds, String title, String standard);

    @Transactional
    void updateQuestion(Long questionId, List<Long> libIds, String title, String standard);

    @Transactional
    void removeQuestion(Long questionId);

    void checkQuestionExists(Long questionId);

    QuestionDetailVO getQuestionDetail(Long questionId);

}
