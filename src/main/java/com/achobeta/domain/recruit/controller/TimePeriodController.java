package com.achobeta.domain.recruit.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.recruit.model.dto.TimePeriodDTO;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.domain.recruitment.service.QuestionnairePeriodService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 18:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/period")
public class TimePeriodController {

    private final RecruitmentActivityService recruitmentActivityService;

    private final TimePeriodService timePeriodService;

    @PostMapping("/add")
    public SystemJsonResponse addTimePeriod(@RequestBody TimePeriodDTO timePeriodDTO) {
        // 校验
        ValidatorUtils.validate(timePeriodDTO);
        Long actId = timePeriodDTO.getActId();
        recruitmentActivityService.checkAndGetRecruitmentActivity(actId);
        // 添加
        Long startTime = timePeriodDTO.getStartTime();
        Long endTime = timePeriodDTO.getEndTime();
        timePeriodService.setPeriodForActivity(actId, startTime, endTime);
        log.info("管理员({}) 为招新({}) 添加时间段 {} -> {}",
                BaseContext.getCurrentUser().getUserId(), actId, startTime, endTime);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("remove/{periodId}")
    public SystemJsonResponse removeTimePeriod(@PathVariable("periodId") @NonNull Long periodId) {
        // 校验
        Long actId = timePeriodService.getActIdByPeriodId(periodId);
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE);
        // 删除
        timePeriodService.removeTimePeriod(periodId);
        log.info("管理员({}) 删除时间段 {}",
                BaseContext.getCurrentUser().getUserId(), periodId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
