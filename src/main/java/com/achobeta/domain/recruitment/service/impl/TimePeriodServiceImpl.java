package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.TimePeriodMapper;
import com.achobeta.domain.recruitment.model.entity.TimePeriod;
import com.achobeta.domain.recruitment.service.TimePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author 马拉圈
* @description 针对表【time_period(时间段表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class TimePeriodServiceImpl extends ServiceImpl<TimePeriodMapper, TimePeriod>
    implements TimePeriodService{

    private final static Integer MIN_GAP = 1;

    private final static Integer MAX_GAP = 2;

    private final static TimeUnit GAP_UNIT = TimeUnit.HOURS;

    private void timePeriodValidate(Long startTime, Long endTime) {
        long gap = endTime - startTime;
        if(startTime.compareTo(System.currentTimeMillis()) < 0 ||
                gap > GAP_UNIT.toMillis(MAX_GAP) || gap < GAP_UNIT.toMillis(MIN_GAP)) {
            throw new GlobalServiceException("时间段非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
    }

    @Override
    public Long addTimePeriod(Long recId, Long startTime, Long endTime) {
        timePeriodValidate(startTime, endTime);
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setRecId(recId);
        timePeriod.setStartTime(new Date(startTime));
        timePeriod.setEndTime(new Date(endTime));
        this.save(timePeriod);
        return timePeriod.getId();
    }

    @Override
    public List<TimePeriod> selectTimePeriods(Long recId) {
        return this.lambdaQuery().eq(TimePeriod::getRecId, recId).list();
    }

    @Override
    public void removeTimePeriod(Long id) {
        this.lambdaUpdate().eq(TimePeriod::getId, id).remove();
    }

}




