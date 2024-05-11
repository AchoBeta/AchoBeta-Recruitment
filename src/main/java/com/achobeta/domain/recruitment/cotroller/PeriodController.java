package com.achobeta.domain.recruitment.cotroller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.recruitment.model.dto.TimePeriodDTO;
import com.achobeta.domain.recruitment.model.entity.TimePeriod;
import com.achobeta.domain.recruitment.model.vo.TimePeriodVO;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.domain.recruitment.service.TimePeriodService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 13:46
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/period")
public class PeriodController {

    private final RecruitmentService recruitmentService;

    private final TimePeriodService timePeriodService;

    @PostMapping("/add")
    public SystemJsonResponse addTimePeriod(@RequestBody TimePeriodDTO timePeriodDTO) {
        // 校验
        ValidatorUtils.validate(timePeriodDTO);
        Long recId = timePeriodDTO.getRecId();
        recruitmentService.checkNotExists(recId);
        // 添加
        Long startTime = timePeriodDTO.getStartTime();
        Long endTime = timePeriodDTO.getEndTime();
        Long periodId = timePeriodService.addTimePeriod(recId, startTime, endTime);
        log.info("管理员({}) 为招新({}) 添加时间段 {} -> {}",
                BaseContext.getCurrentUser().getUserId(), recId, startTime, endTime);
        return SystemJsonResponse.SYSTEM_SUCCESS(periodId);
    }

    @GetMapping("list/{recId}")
    public SystemJsonResponse selectTimePeriods(@PathVariable("recId") @NonNull Long recId) {
        // 校验
        recruitmentService.checkNotExists(recId);
        // 查询
        List<TimePeriod> timePeriods = timePeriodService.selectTimePeriods(recId);
        List<TimePeriodVO> timePeriodVOS = BeanUtil.copyToList(timePeriods, TimePeriodVO.class);
        log.info("查询招新({}) 的时间段， 共 {} 段", recId, timePeriodVOS.size());
        return SystemJsonResponse.SYSTEM_SUCCESS(timePeriodVOS);
    }


    @GetMapping("remove/{id}")
    public SystemJsonResponse removeTimePeriod(@PathVariable("id") @NonNull Long id) {
        timePeriodService.removeTimePeriod(id);
        log.info("管理员({}) 删除时间段 {}",
                BaseContext.getCurrentUser().getUserId(), id);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
