package com.achobeta.domain.recruitment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.paper.service.PaperEntryService;
import com.achobeta.domain.recruitment.model.dao.mapper.RecruitmentActivityMapper;
import com.achobeta.domain.recruitment.model.entity.Questionnaire;
import com.achobeta.domain.recruitment.model.entity.QuestionnaireEntry;
import com.achobeta.domain.recruitment.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruitment.service.RecruitmentActivityService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author 马拉圈
* @description 针对表【recruitment_activity(招新活动表)】的数据库操作Service实现
* @createDate 2024-05-15 11:17:37
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentActivityServiceImpl extends ServiceImpl<RecruitmentActivityMapper, RecruitmentActivity>
    implements RecruitmentActivityService{

    private final RecruitmentActivityMapper recruitmentActivityMapper;

    private final PaperEntryService paperEntryService;

    @Override
    public Long createRecruitmentActivity(Integer batch, Date deadline) {
        RecruitmentActivity recruitmentActivity = new RecruitmentActivity();
        recruitmentActivity.setBatch(batch);
        recruitmentActivity.setDeadline(deadline);
        System.out.println(recruitmentActivity);
        this.save(recruitmentActivity);
        return recruitmentActivity.getId();
    }

    @Override
    public Optional<RecruitmentActivity> getRecruitmentActivity(Long recId) {
        return this.lambdaQuery()
                .eq(RecruitmentActivity::getId, recId)
                .oneOpt();
    }

    @Override
    public void checkRecruitmentExists(Long recId) {
        getRecruitmentActivity(recId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_NOT_EXISTS));
    }

    @Override
    public RecruitmentActivity checkAndGetRecruitment(Long recId) {
        return getRecruitmentActivity(recId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_NOT_EXISTS));
    }

    @Override
    public void checkActivityNotRun(Long recId) {
        Boolean isRun = checkAndGetRecruitment(recId).getIsRun();
        if(Boolean.TRUE.equals(isRun)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_IS_RUN);
        }
    }

    @Override
    public void checkActivityRun(Long recId) {
        Boolean isRun = checkAndGetRecruitment(recId).getIsRun();
        if(Boolean.FALSE.equals(isRun)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_IS_NOT_RUN);
        }
    }

    @Override
    public List<Long> getStuIdsByRecId(Long recId) {
        return recruitmentActivityMapper.getStuIdsByRecId(recId);
    }

    @Override
    public List<Long> getQuestionnaireIds(Long recId) {
        return Db.lambdaQuery(Questionnaire.class)
                .eq(Questionnaire::getRecId, recId)
                .list()
                .stream()
                .map(Questionnaire::getId)
                .toList();
    }

    @Override
    public List<Long> getRecIdsByPaperId(Long paperId) {
        return this.lambdaQuery()
                .eq(RecruitmentActivity::getPaperId, paperId)
                .list()
                .stream()
                .parallel()
                .map(RecruitmentActivity::getId)
                .toList();
    }

    @Override
    public void shiftRecruitmentActivity(Long recId, Boolean isRun) {
        this.lambdaUpdate()
                .eq(RecruitmentActivity::getId, recId)
                .set(RecruitmentActivity::getIsRun, isRun)
                .update();
    }

    @Override
    public void setRecruitmentQuestionPaper(Long recId, Long paperId) {
        // 获得老的 paperId
        Long olderPaperId = checkAndGetRecruitment(recId).getPaperId();
        // 如果不一样才有必要进行以下操作
        if(!paperId.equals(olderPaperId)) {
            // 获取用户填的 id 列表，删除原本的所有回答（哪怕两份卷子有相同的问题，也照样删除）
            List<Long> questionnaireIds = getQuestionnaireIds(recId);
            if(CollectionUtil.isEmpty(questionnaireIds)) {
                return;
            }
            Db.lambdaUpdate(QuestionnaireEntry.class)
                    .in(QuestionnaireEntry::getQuestionnaireId, questionnaireIds)
                    .remove();
            // 设置题单
            this.lambdaUpdate()
                    .eq(RecruitmentActivity::getId, recId)
                    .set(RecruitmentActivity::getPaperId, paperId)
                    .update();
        }
    }

    @Override
    public List<QuestionEntryVO> getPaperQuestions(Long recId) {
        Long paperId = checkAndGetRecruitment(recId).getPaperId();
        if(Objects.isNull(paperId)) {
            return new ArrayList<>();
        }
        PaperQuestionsVO questionsOnPaper = paperEntryService.getQuestionsOnPaper(paperId);
        if(Objects.isNull(questionsOnPaper)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.QUESTION_PAPER_NOT_EXISTS);
        }
        return questionsOnPaper.getQuestions();
    }
}
