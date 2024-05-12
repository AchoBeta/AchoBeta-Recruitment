package com.achobeta.domain.recruitment.cotroller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dto.RecruitmentDTO;
import com.achobeta.domain.recruitment.model.entity.CustomEntry;
import com.achobeta.domain.recruitment.model.entity.Recruitment;
import com.achobeta.domain.recruitment.model.entity.TimePeriod;
import com.achobeta.domain.recruitment.model.vo.CustomEntryVO;
import com.achobeta.domain.recruitment.model.vo.RecruitmentModelVO;
import com.achobeta.domain.recruitment.model.vo.RecruitmentVO;
import com.achobeta.domain.recruitment.model.vo.TimePeriodVO;
import com.achobeta.domain.recruitment.service.CustomEntryService;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.domain.recruitment.service.TimePeriodService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ValidatorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 11:42
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    private final CustomEntryService customEntryService;

    private final TimePeriodService timePeriodService;

    @PostMapping("/create")
    public SystemJsonResponse createRecruitment(@RequestBody RecruitmentDTO recruitmentDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentDTO);
        Integer batch = recruitmentDTO.getBatch();
        if(batch.compareTo(0) <= 0) {
            throw new GlobalServiceException("ab版本非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
        // 调用服务创建一次招新活动
        Date deadline = new Date(recruitmentDTO.getDeadline());
        Long recruitmentId = recruitmentService.createRecruitment(batch, deadline);
        log.info("管理员({}) 创建了一次招新，版本：{} {}，id：{}",
                BaseContext.getCurrentUser().getUserId(), batch, deadline, recruitmentId);
        // 返回招新id
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentId);
    }

    @GetMapping("/list")
    public SystemJsonResponse getRecruitments(@RequestParam(name = "isRun", required = false) Boolean isRun) {
        List<Recruitment> list = null;
        if(Objects.isNull(isRun)) {
            list = recruitmentService.list();
        } else {
            list = recruitmentService.lambdaQuery().eq(Recruitment::getIsRun, isRun).list();
        }
        List<RecruitmentVO> recruitmentVOS = BeanUtil.copyToList(list, RecruitmentVO.class);
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentVOS);
    }

    @GetMapping("/get/{recId}")
    public SystemJsonResponse getCustomDefine(@PathVariable("recId") @NonNull Long recId) {
        Recruitment recruitment = recruitmentService.getById(recId);
        if(Objects.isNull(recruitment)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_NOT_EXISTS);
        }
        // 查询自定义项
        List<CustomEntry> customEntries = customEntryService.selectCustomEntry(recId);
        // 查询时间段
        List<TimePeriod> timePeriods = timePeriodService.selectTimePeriods(recId);
        // 构造招新活动的自定义问卷模板
        RecruitmentModelVO recruitmentModelVO = RecruitmentModelVO.builder()
                .recruitmentVO(BeanUtil.copyProperties(recruitment, RecruitmentVO.class))
                .customEntryVOS(BeanUtil.copyToList(customEntries, CustomEntryVO.class))
                .timePeriodVOS(BeanUtil.copyToList(timePeriods, TimePeriodVO.class))
                .build();
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentModelVO);
    }

    @GetMapping("/shift/{recId}")
    public SystemJsonResponse shiftRecruitment(@PathVariable("recId") @NonNull Long recId,
                                               @RequestParam(name = "isRun") @NonNull Boolean isRun) {
        // 检测
        recruitmentService.checkNotExists(recId);
        // 修改
        recruitmentService.shiftRecruitment(recId, isRun);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
