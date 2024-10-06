package com.achobeta.domain.question.service;

import com.achobeta.domain.question.model.dto.QuestionQueryDTO;
import com.achobeta.domain.question.model.dto.QuestionSaveBatchDTO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.achobeta.domain.question.model.vo.QuestionQueryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question(问题表)】的数据库操作Service
* @createDate 2024-07-05 20:03:35
*/
public interface QuestionService extends IService<Question> {

    QuestionQueryVO queryQuestions(QuestionQueryDTO questionQueryDTO);

    Optional<Question> getQuestion(Long questionId);

    Long addQuestion(List<Long> libIds, String title, String standard);

    void saveBatchQuestion(QuestionSaveBatchDTO questionSaveBatchDTO);

    void updateQuestion(Long questionId, List<Long> libIds, String title, String standard);

    void removeQuestion(Long questionId);

    void checkQuestionExists(Long questionId);

    QuestionDetailVO getQuestionDetail(Long questionId);

}
