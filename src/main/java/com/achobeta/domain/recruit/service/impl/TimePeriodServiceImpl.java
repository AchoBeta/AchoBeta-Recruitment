package com.achobeta.domain.recruit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruit.model.converter.TimePeriodConverter;
import com.achobeta.domain.recruit.model.dao.mapper.TimePeriodMapper;
import com.achobeta.domain.recruit.model.entity.TimePeriod;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author 马拉圈
* @description 针对表【time_period(时间段表)】的数据库操作Service实现
* @createDate 2024-07-06 12:33:02
*/
@Service
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
    public List<TimePeriodVO> getTimePeriodsByActId(Long actId) {
        List<TimePeriod> timePeriods = this.lambdaQuery().eq(TimePeriod::getActId, actId).list();
        return TimePeriodConverter.INSTANCE.timePeriodListToTimePeriodVOList(timePeriods);
    }

    @Override
    public Long getActIdByPeriodId(Long periodId) {
        return this.lambdaQuery()
                .eq(TimePeriod::getId, periodId)
                .oneOpt()
                .orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.PERIOD_NOT_EXISTS))
                .getActId();
    }

    @Override
    public void setPeriodForActivity(Long actId, Long startTime, Long endTime) {
        timePeriodValidate(startTime, endTime);
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setActId(actId);
        timePeriod.setStartTime(new Date(startTime));
        timePeriod.setEndTime(new Date(endTime));
        this.save(timePeriod);
    }

    @Override
    public void removeTimePeriod(Long periodId) {
        this.lambdaUpdate().eq(TimePeriod::getId, periodId).remove();
    }
}




