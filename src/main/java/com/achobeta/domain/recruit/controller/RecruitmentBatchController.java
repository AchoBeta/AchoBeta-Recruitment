package com.achobeta.domain.recruit.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruit.model.dto.RecruitmentBatchDTO;
import com.achobeta.domain.recruit.model.dto.RecruitmentBatchUpdateDTO;
import com.achobeta.domain.recruit.model.entity.RecruitmentBatch;
import com.achobeta.domain.recruit.model.vo.RecruitmentBatchVO;
import com.achobeta.domain.recruit.service.RecruitmentBatchService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 18:14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit/batch")
public class RecruitmentBatchController {

    private final RecruitmentBatchService recruitmentBatchService;

    @PostMapping("/create")
    public SystemJsonResponse createRecruitmentBatch(@RequestBody RecruitmentBatchDTO recruitmentBatchDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentBatchDTO);
        Integer batch = recruitmentBatchDTO.getBatch();
        if(batch.compareTo(0) <= 0) {
            throw new GlobalServiceException("ab版本非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
        // 调用服务创建一次招新活动
        String title = recruitmentBatchDTO.getTitle();
        Date deadline = new Date(recruitmentBatchDTO.getDeadline());
        Long batchId = recruitmentBatchService.createRecruitmentBatch(batch, title, deadline);
        return SystemJsonResponse.SYSTEM_SUCCESS(batchId);
    }

    @PostMapping("/update")
    public SystemJsonResponse updateRecruitmentBatch(@RequestBody RecruitmentBatchUpdateDTO recruitmentBatchUpdateDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentBatchUpdateDTO);
        Long batchId = recruitmentBatchUpdateDTO.getBatchId();
        recruitmentBatchService.checkAndGetRecruitmentBatchIsRun(batchId, Boolean.FALSE);
        String title = recruitmentBatchUpdateDTO.getTitle();
        Date deadline = new Date(recruitmentBatchUpdateDTO.getDeadline());
        // 更新
        recruitmentBatchService.updateRecruitmentBatch(batchId, title, deadline);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list")
    public SystemJsonResponse getRecruitBatches(@RequestParam(name = "isRun", required = false) Boolean isRun) {
        List<RecruitmentBatch> recruitmentBatches = recruitmentBatchService.getRecruitmentBatches(isRun);
        List<RecruitmentBatchVO> recruitmentBatchVOS = BeanUtil.copyToList(recruitmentBatches, RecruitmentBatchVO.class);
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentBatchVOS);
    }

    @GetMapping("/shift/{batchId}")
    public SystemJsonResponse shiftRecruitmentBatch(@PathVariable("batchId") @NotNull Long batchId,
                                               @RequestParam(name = "isRun") @NotNull Boolean isRun) {
        // 检测
        recruitmentBatchService.checkRecruitmentBatchExists(batchId);
        // 修改
        recruitmentBatchService.shiftRecruitmentBatch(batchId, isRun);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }


}
