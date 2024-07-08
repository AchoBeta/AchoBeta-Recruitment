package com.achobeta.domain.recruit.service;

import com.achobeta.domain.recruit.model.dto.QuestionAnswerDTO;
import com.achobeta.domain.recruit.model.entity.ParticipationQuestionLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【participation_question_link(“活动参与”-问题关联表)】的数据库操作Service
* @createDate 2024-07-06 12:33:02
*/
public interface ParticipationQuestionLinkService extends IService<ParticipationQuestionLink> {

    // 查询 ------------------------------------------

    Optional<ParticipationQuestionLink> getParticipationQuestionLink(Long participationId, Long questionId);

    // 写入 ------------------------------------------

    void putQuestionAnswers(Long participationId, List<QuestionAnswerDTO> questionAnswerDTOS);

}
