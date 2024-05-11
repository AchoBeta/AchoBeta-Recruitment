package com.achobeta.domain.recruitment.cotroller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.recruitment.model.dto.CustomEntryDTO;
import com.achobeta.domain.recruitment.model.entity.CustomEntry;
import com.achobeta.domain.recruitment.model.vo.CustomEntryVO;
import com.achobeta.domain.recruitment.service.CustomEntryService;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 15:29
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entry")
public class EntryController {

    private final RecruitmentService recruitmentService;

    private final CustomEntryService customEntryService;

    @PostMapping("/add")
    public SystemJsonResponse addTimePeriod(@RequestBody CustomEntryDTO customEntryDTO) {
        // 校验
        ValidatorUtils.validate(customEntryDTO);
        Long recId = customEntryDTO.getRecId();
        recruitmentService.checkNotExists(recId);
        // 添加
        String title = customEntryDTO.getTitle();
        Long id = customEntryService.addCustomEntry(recId, title);
        log.info("管理员({}) 为招新({}) 添加自定义项 {}",
                BaseContext.getCurrentUser().getUserId(), recId, title);
        return SystemJsonResponse.SYSTEM_SUCCESS(id);
    }

    @GetMapping("list/{recId}")
    public SystemJsonResponse selectTimePeriods(@PathVariable("recId") @NonNull Long recId) {
        // 校验
        recruitmentService.checkNotExists(recId);
        // 查询
        List<CustomEntry> customEntries = customEntryService.selectCustomEntry(recId);
        List<CustomEntryVO> customEntryVOS = BeanUtil.copyToList(customEntries, CustomEntryVO.class);
        log.info("查询招新({}) 的自定义项， 共 {} 项", recId, customEntryVOS.size());
        return SystemJsonResponse.SYSTEM_SUCCESS(customEntryVOS);
    }

    @GetMapping("remove/{id}")
    public SystemJsonResponse removeTimePeriod(@PathVariable("id") @NonNull Long id) {
        customEntryService.removeCustomEntry(id);
        log.info("管理员({}) 删除自定义项 {}",
                BaseContext.getCurrentUser().getUserId(), id);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
