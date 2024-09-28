package com.achobeta.domain.feishu.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.feishu.model.dto.FeishuResourceQueryDTO;
import com.achobeta.domain.feishu.model.vo.FeishuResourceQueryVO;
import com.achobeta.domain.feishu.service.FeishuResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 16:09
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feishu/resource")
@Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
public class FeishuResourceController {

    private final FeishuResourceService feishuResourceService;

    @GetMapping("/{ticket}")
    public RedirectView redirectByTicket(@PathVariable("ticket") @NotBlank String ticket) {
        String url = feishuResourceService.redirectByTicket(ticket);
        return new RedirectView(url);
    }

    @PostMapping("/query")
    public SystemJsonResponse queryResource(@Valid @RequestBody(required = false) FeishuResourceQueryDTO feishuResourceQueryDTO) {
        FeishuResourceQueryVO feishuResourceQueryVO = feishuResourceService.queryResource(feishuResourceQueryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(feishuResourceQueryVO);
    }

}
