package com.achobeta.domain.recruit.service;

import com.achobeta.domain.recruit.model.entity.ParticipationPeriodLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【participation_period_link(“活动参与”-时间段关联表)】的数据库操作Service
* @createDate 2024-07-06 12:33:02
*/
public interface ParticipationPeriodLinkService extends IService<ParticipationPeriodLink> {

    // 查询 ------------------------------------------

    Optional<ParticipationPeriodLink> getParticipationPeriodLink(Long participationId, Long periodId);

    // 写入 ------------------------------------------

    void putTimePeriods(Long participationId, List<Long> periodIds);

}
