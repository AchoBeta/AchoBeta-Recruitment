package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.dto.QuestionnaireDTO;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【questionnaire(问卷表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface QuestionnaireService extends IService<Questionnaire> {

    Optional<Questionnaire> getQuestionnaire(Long questionnaireId);

    QuestionnaireVO createQuestionnaire(Long stuId, Long recId);

    QuestionnaireVO getQuestionnaire(Long stuId, Long recId);

    @Transactional
    void submitQuestionnaire(QuestionnaireDTO questionnaireDTO);

    void checkUser(Long stuId, Long questionnaireId);

    Long getQuestionnaireRecId(Long questionnaireId);

    List<Long> getQuestionnaireIdsByPaperId(Long paperId);

}
