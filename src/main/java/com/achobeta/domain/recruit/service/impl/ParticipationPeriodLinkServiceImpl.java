package com.achobeta.domain.recruit.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruit.model.dao.mapper.ParticipationPeriodLinkMapper;
import com.achobeta.domain.recruit.model.entity.ActivityParticipation;
import com.achobeta.domain.recruit.model.entity.ParticipationPeriodLink;
import com.achobeta.domain.recruit.service.ParticipationPeriodLinkService;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【participation_period_link(“活动参与”-时间段关联表)】的数据库操作Service实现
* @createDate 2024-07-06 12:33:02
*/
@Service
@RequiredArgsConstructor
public class ParticipationPeriodLinkServiceImpl extends ServiceImpl<ParticipationPeriodLinkMapper, ParticipationPeriodLink>
    implements ParticipationPeriodLinkService{

    private final TimePeriodService timePeriodService;

    private Long getActivityParticipationRctId(Long participationId) {
        return Db.lambdaQuery(ActivityParticipation.class)
                .eq(ActivityParticipation::getId, participationId)
                .oneOpt().orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_ACTIVITY_NOT_EXISTS))
                .getActId();
    }

    private void addParticipationPeriodLink(Long participationId, Long periodId) {
        getParticipationPeriodLink(participationId, periodId)
                .ifPresentOrElse(participationPeriodLink -> {
                }, () -> {
                    // 插入一条新的
                    ParticipationPeriodLink participationPeriodLink = new ParticipationPeriodLink();
                    participationPeriodLink.setParticipationId(participationId);
                    participationPeriodLink.setPeriodId(periodId);
                    this.save(participationPeriodLink);
                });
    }

    private void removeParticipationPeriodLink(Long participationId, Long periodId) {
        // 删除一条旧的
        this.lambdaUpdate()
                .eq(ParticipationPeriodLink::getParticipationId, participationId)
                .eq(ParticipationPeriodLink::getPeriodId, periodId)
                .remove();
    }

    @Override
    public Optional<ParticipationPeriodLink> getParticipationPeriodLink(Long participationId, Long periodId) {
        return this.lambdaQuery()
                .eq(ParticipationPeriodLink::getParticipationId, participationId)
                .eq(ParticipationPeriodLink::getPeriodId, periodId)
                .oneOpt();
    }

    @Override
    public void putTimePeriods(Long participationId, List<Long> periodIds) {
        Long recId = getActivityParticipationRctId(participationId);
        Map<Long, Boolean> hash = new HashMap<>();
        timePeriodService.getTimePeriodsByActId(recId).forEach(timePeriod -> {
            hash.put(timePeriod.getId(), Boolean.FALSE);
        });
        // 将选中的时间段设置为 true（不存在于 map 的忽略即可），并且不会重复~
        periodIds.stream().filter(hash::containsKey).forEach(periodId -> {
            hash.put(periodId, Boolean.TRUE);
        });
        // map 中 true 的尝试添加，false 的尝试删除
        hash.forEach((periodId, flag) -> {
            if (Boolean.TRUE.equals(flag)) {
                addParticipationPeriodLink(participationId, periodId);
            } else {
                removeParticipationPeriodLink(participationId, periodId);
            }
        });
    }
}




