package com.achobeta.domain.recruit.service;

import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.model.condition.StudentGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【recruitment_activity(招新活动表)】的数据库操作Service
* @createDate 2024-07-06 12:33:02
*/
public interface RecruitmentActivityService extends IService<RecruitmentActivity> {

    // 查询 ------------------------------------------

    Optional<RecruitmentActivity> getRecruitmentActivity(Long actId);

    List<QuestionVO> getQuestionsByActId(Long actId);

    List<Long> getParticipationIdsByActId(Long actId);

    /**
     * 获取以 paperId 为试卷的所有招新活动
     * @param paperId
     * @return
     */
    List<Long> getActIdsByPaperId(Long paperId);

    /**
     * 活动招新活动列表
     * @param isRun （null：所有、true：启动了的、false：未启动的）
     * @return
     */
    List<RecruitmentActivity> getRecruitmentActivities(Long batchId, Boolean isRun);

    // 用户端查看
    List<RecruitmentActivity> getRecruitmentActivities(Long batchId, Long stuId);

    // 写入 ------------------------------------------

    Long createRecruitmentActivity(Long batchId, StudentGroup target,
                                   String title, String description, Date deadline);

    void updateRecruitmentActivity(Long actId, StudentGroup target,
                                   String title, String description, Date deadline);

    void shiftRecruitmentActivity(Long actId, Boolean isRun);

    /**
     * 为招新活动设置一张试卷（给用户回答的特殊问题）
     * @param actId
     * @param paperId
     */
    @Transactional
    void setPaperForActivity(Long actId, Long paperId);

    // 检测 ------------------------------------------

    void checkRecruitmentActivityExists(Long actId);

    RecruitmentActivity checkAndGetRecruitmentActivity(Long actId);

    RecruitmentActivity checkAndGetRecruitmentActivityIsRun(Long actId, Boolean isRun);

    void checkCanUserParticipateInActivity(Long stuId, Long actId);

}
