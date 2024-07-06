package com.achobeta.domain.recruit.service;

import com.achobeta.domain.recruit.model.entity.TimePeriod;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【time_period(时间段表)】的数据库操作Service
* @createDate 2024-07-06 12:33:02
*/
public interface TimePeriodService extends IService<TimePeriod> {

    // 查询 ------------------------------------------

    List<TimePeriodVO> getTimePeriodsByActId(Long actId);

    Long getActIdByPeriodId(Long periodId);

    // 写入 ------------------------------------------

    void setPeriodForActivity(Long actId, Long startTime, Long endTime);

    void removeTimePeriod(Long periodId);

}
