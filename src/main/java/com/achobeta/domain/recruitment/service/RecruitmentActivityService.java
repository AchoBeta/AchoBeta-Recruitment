package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.recruitment.model.entity.RecruitmentActivity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【recruitment_activity(招新活动表)】的数据库操作Service
* @createDate 2024-05-15 11:17:37
*/
public interface RecruitmentActivityService extends IService<RecruitmentActivity> {

    Long createRecruitmentActivity(Integer batch, Date deadline);

    Optional<RecruitmentActivity> getRecruitmentActivity(Long recId);

    /**
     * 判断招新活动是否存在
     * @param recId
     */
    void checkRecruitmentExists(Long recId);

    /**
     * 判断招新活动是否存在，如果存在则返回
     * @param recId
     */
    RecruitmentActivity checkAndGetRecruitment(Long recId);

    /**
     * 判断招新活动是否是未开始的状态，如果不是则抛异常
     * @param recId
     */
    void checkActivityNotRun(Long recId);

    /**
     * 判断招新活动是否是开始的状态，如果不是则抛异常
     * @param recId
     */
    void checkActivityRun(Long recId);

    List<Long> getStuIdsByRecId(Long recId);

    List<Long> getQuestionnaireIds(Long recId);

    List<Long> getRecIdsByPaperId(Long paperId);

    void shiftRecruitmentActivity(Long recId, Boolean isRun);

    @Transactional
    void setRecruitmentQuestionPaper(Long recId, Long paperId);

    List<QuestionEntryVO> getPaperQuestions(Long recId);

}
