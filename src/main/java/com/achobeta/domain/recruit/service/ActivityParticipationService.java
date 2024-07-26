package com.achobeta.domain.recruit.service;

import com.achobeta.domain.recruit.model.dto.QuestionAnswerDTO;
import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.vo.ParticipationPeriodVO;
import com.achobeta.domain.recruit.model.vo.ParticipationVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【activity_participation(“活动参与”表)】的数据库操作Service
* @createDate 2024-07-06 12:33:02
*/
public interface ActivityParticipationService extends IService<ActivityParticipation> {

    // 查询 ------------------------------------------

    Optional<ActivityParticipation> getActivityParticipation(Long participationId);

    // 若不存在，会创建个默认的
    ParticipationVO getActivityParticipation(Long stuId, Long actId);

    Long getActIdByParticipationId(Long participationId);

    List<Long> getStuIdsByActId(Long actId);

    /**
     * 获得以 paperId 试卷的所有“活动参与”
     * @param paperId
     * @return
     */
    List<Long> getParticipationIdsByPaperId(Long paperId);

    List<ParticipationPeriodVO> getParticipationPeriods(List<Long> participationIds);

    // 写入 ------------------------------------------

    ParticipationVO createActivityParticipation(Long stuId, Long actId);

    @Transactional
    void participateInActivity(Long participationId,
                               List<QuestionAnswerDTO> questionAnswerVOS, List<Long> periodIds);

    // 检测 ------------------------------------------

    void checkActivityParticipationUser(Long stuId, Long participationId);

    void checkParticipationExists(Long participationId);

}
