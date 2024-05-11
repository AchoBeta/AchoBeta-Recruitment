package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.entity.TimePeriod;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
* @author 马拉圈
* @description 针对表【time_period(时间段表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface TimePeriodService extends IService<TimePeriod> {

    Long addTimePeriod(Long recId, Long startTime, Long endTime);

    List<TimePeriod> selectTimePeriods(Long recId);

    void removeTimePeriod(Long id);

}
