package com.achobeta.domain.recruit.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruit.model.dao.mapper.ParticipationQuestionLinkMapper;
import com.achobeta.domain.recruit.model.dto.QuestionAnswerDTO;
import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.entity.ParticipationQuestionLink;
import com.achobeta.domain.recruit.service.ParticipationQuestionLinkService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【participation_question_link(“活动参与”-问题关联表)】的数据库操作Service实现
* @createDate 2024-07-06 12:33:02
*/
@Service
@RequiredArgsConstructor
public class ParticipationQuestionLinkServiceImpl extends ServiceImpl<ParticipationQuestionLinkMapper, ParticipationQuestionLink>
    implements ParticipationQuestionLinkService{

    private final static String DEFAULT_ANSWER = "";

    private final RecruitmentActivityService recruitmentActivityService;

    private Long getActivityParticipationActId(Long participationId) {
        return Db.lambdaQuery(ActivityParticipation.class)
                .eq(ActivityParticipation::getId, participationId)
                .oneOpt().orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_ACTIVITY_NOT_EXISTS))
                .getActId();
    }

    private void addParticipationQuestionLink(Long participationId, Long questionId, String answer) {
        ParticipationQuestionLink participationQuestionLink = new ParticipationQuestionLink();
        participationQuestionLink.setParticipationId(participationId);
        participationQuestionLink.setQuestionId(questionId);
        participationQuestionLink.setAnswer(answer);
        this.save(participationQuestionLink);
    }

    private void updateParticipationQuestionLink(Long participationId, Long questionId, String answer) {
        this.lambdaUpdate()
                .eq(ParticipationQuestionLink::getParticipationId, participationId)
                .eq(ParticipationQuestionLink::getQuestionId, questionId)
                .set(ParticipationQuestionLink::getAnswer, answer)
                .update();
    }

    private void addOrUpdateParticipationQuestionLink(Long participationId, Long questionId, String answer) {
        getParticipationQuestionLink(participationId, questionId)
                .ifPresentOrElse(participationQuestionLink -> {
                    updateParticipationQuestionLink(participationId, questionId, answer);
                }, () -> {
                    addParticipationQuestionLink(participationId, questionId, answer);
                });
    }

    @Override
    public Optional<ParticipationQuestionLink> getParticipationQuestionLink(Long participationId, Long questionId) {
        return this.lambdaQuery()
                .eq(ParticipationQuestionLink::getParticipationId, participationId)
                .eq(ParticipationQuestionLink::getQuestionId, questionId)
                .oneOpt();
    }

    @Override
    public void putQuestionAnswers(Long participationId, List<QuestionAnswerDTO> questionAnswerDTOS) {
        Long recId = getActivityParticipationActId(participationId);
        Map<Long, String> hash = new HashMap<>();
        // 获取答题模板
        recruitmentActivityService.getQuestionsByActId(recId).forEach(questionVO -> {
            hash.put(questionVO.getId(), DEFAULT_ANSWER);
        });
        // 将答案填入模板
        questionAnswerDTOS.stream()
                .filter(questionAnswerDTO -> hash.containsKey(questionAnswerDTO.getQuestionId()))
                .filter(questionAnswerDTO -> StringUtils.hasText(questionAnswerDTO.getAnswer()))
                .forEach(questionAnswerDTO -> {
                    hash.put(questionAnswerDTO.getQuestionId(), questionAnswerDTO.getAnswer());
                });
        hash.forEach((questionId, answer) -> {
            addParticipationQuestionLink(participationId, questionId, answer);
        });
    }
}




