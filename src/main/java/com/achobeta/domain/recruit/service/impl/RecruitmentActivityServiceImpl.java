package com.achobeta.domain.recruit.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.recruit.model.condition.StudentGroup;
import com.achobeta.domain.recruit.model.dao.mapper.RecruitmentActivityMapper;
import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.entity.ParticipationQuestionLink;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
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
* @createDate 2024-07-06 12:33:02
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class RecruitmentActivityServiceImpl extends ServiceImpl<RecruitmentActivityMapper, RecruitmentActivity>
    implements RecruitmentActivityService{

    private final PaperQuestionLinkService paperQuestionLinkService;

    private final QuestionPaperService questionPaperService;

    private final StuResumeService stuResumeService;

    @Override
    public Optional<RecruitmentActivity> getRecruitmentActivity(Long actId) {
        return this.lambdaQuery().eq(RecruitmentActivity::getId, actId).oneOpt();
    }

    @Override
    public List<QuestionVO> getQuestionsByActId(Long actId) {
        Long paperId = checkAndGetRecruitmentActivity(actId).getPaperId();
        if(Objects.isNull(paperId)) {
            return new ArrayList<>();
        }
        return paperQuestionLinkService.getQuestionsOnPaper(paperId);
    }

    @Override
    public List<Long> getParticipationIdsByActId(Long actId) {
        return Db.lambdaQuery(ActivityParticipation.class)
                .eq(ActivityParticipation::getActId, actId)
                .list()
                .stream()
                .map(ActivityParticipation::getId)
                .toList();
    }

    @Override
    public List<Long> getActIdsByPaperId(Long paperId) {
        return this.lambdaQuery()
                .eq(RecruitmentActivity::getPaperId, paperId)
                .list()
                .stream()
                .map(RecruitmentActivity::getId)
                .toList();
    }

    @Override
    public List<RecruitmentActivity> getRecruitmentActivities(Long batchId, Boolean isRun) {
        return this.lambdaQuery()
                .eq(RecruitmentActivity::getBatchId, batchId)
                .eq(Objects.nonNull(isRun), RecruitmentActivity::getIsRun, isRun)
                .list();
    }

    @Override
    public List<RecruitmentActivity> getRecruitmentActivities(Long batchId, Long stuId) {
        try {
            StuResume stuResume = stuResumeService.checkAndGetStuResumeByBatchIdAndStuId(batchId, stuId);
            // 过滤出用户可见的
            return getRecruitmentActivities(batchId, Boolean.TRUE).stream().filter(recruitmentActivity -> {
                StudentGroup target = recruitmentActivity.getTarget();
                return target.predicate().test(stuResume);
            }).toList();
        } catch (GlobalServiceException e) {
            log.warn("stuId: {} batchId: {}, exception: {}", stuId, batchId, e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Long createRecruitmentActivity(Long batchId, StudentGroup target, String title, String description, Date deadline) {
        RecruitmentActivity recruitmentActivity = new RecruitmentActivity();
        recruitmentActivity.setBatchId(batchId);
        recruitmentActivity.setTarget(target);
        recruitmentActivity.setTitle(title);
        recruitmentActivity.setDescription(description);
        recruitmentActivity.setDeadline(deadline);
        this.save(recruitmentActivity);
        return recruitmentActivity.getId();
    }

    @Override
    public void updateRecruitmentActivity(Long actId, StudentGroup target, String title, String description, Date deadline) {
        RecruitmentActivity updateRecruitmentActivity = new RecruitmentActivity();
        updateRecruitmentActivity.setTarget(target);
        updateRecruitmentActivity.setTitle(title);
        updateRecruitmentActivity.setDescription(description);
        updateRecruitmentActivity.setDeadline(deadline);
        this.lambdaUpdate()
                .eq(RecruitmentActivity::getId, actId)
                .update(updateRecruitmentActivity);
    }

    @Override
    public void shiftRecruitmentActivity(Long actId, Boolean isRun) {
        this.lambdaUpdate()
                .eq(RecruitmentActivity::getId, actId)
                .set(RecruitmentActivity::getIsRun, isRun)
                .update();
    }

    @Override
    public void setPaperForActivity(Long actId, Long paperId) {
        // 保证招新未开始
        Long oldPaperId = checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE).getPaperId();
        // 检测
        questionPaperService.checkPaperExists(paperId);
        // 只有新试卷和老试卷不一样才有必要进行
        if(!paperId.equals(oldPaperId)) {
            // 获取此活动已有的，“活动参与”列表，删除原本的所有回答（哪怕两份卷子有相同的问题，也照样删除）
            List<Long> participationIds = getParticipationIdsByActId(actId);
            if (!CollectionUtil.isEmpty(participationIds)) {
                Db.lambdaUpdate(ParticipationQuestionLink.class)
                        .in(ParticipationQuestionLink::getParticipationId, participationIds)
                        .remove();
            }
            // 设置新试卷
            this.lambdaUpdate()
                    .eq(RecruitmentActivity::getId, actId)
                    .set(RecruitmentActivity::getPaperId, paperId)
                    .update();
        }
    }

    @Override
    public void checkRecruitmentActivityExists(Long actId) {
        getRecruitmentActivity(actId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_ACTIVITY_NOT_EXISTS));
    }

    @Override
    public RecruitmentActivity checkAndGetRecruitmentActivity(Long actId) {
        return getRecruitmentActivity(actId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_ACTIVITY_NOT_EXISTS));
    }

    @Override
    public RecruitmentActivity checkAndGetRecruitmentActivityIsRun(Long actId, Boolean isRun) {
        RecruitmentActivity recruitmentActivity = checkAndGetRecruitmentActivity(actId);
        if(!recruitmentActivity.getIsRun().equals(isRun)) {
            if(Boolean.TRUE.equals(isRun)) {
                // recruitmentActivity 为 false，未启动
                throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_ACTIVITY_IS_NOT_RUN);
            }else {
                // recruitmentActivity 为 true，未启动
                throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_ACTIVITY_IS_RUN);
            }
        }
        return recruitmentActivity;
    }

    @Override
    public void checkCanUserParticipateInActivity(Long stuId, Long actId) {
        RecruitmentActivity recruitmentActivity = checkAndGetRecruitmentActivityIsRun(actId, Boolean.TRUE);
        Long batchId = recruitmentActivity.getBatchId();
        StudentGroup target = recruitmentActivity.getTarget();
        StuResume stuResume = stuResumeService.checkAndGetStuResumeByBatchIdAndStuId(batchId, stuId);
        if(!target.predicate().test(stuResume)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_CANNOT_PARTICIPATE_IN_ACTIVITY);
        }
    }
}




