package com.achobeta.domain.shortlink.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.annotation.IsAccessible;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.shortlink.model.dto.ShortLinkQueryDTO;
import com.achobeta.domain.shortlink.model.vo.ShortLinkQueryVO;
import com.achobeta.domain.shortlink.service.ShortLinkService;
import com.achobeta.util.HttpServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@Intercept(permit = {UserTypeEnum.ADMIN})
@RequestMapping("/api/v1/shortlink")
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 重定向短链接
     *
     * @param code 短链code
     * @return 重定向到原链接
     */
    @GetMapping("/{code}")
    @Intercept(ignore = true)
    public RedirectView getShortLink(@PathVariable("code") @NotBlank String code) {
        String originUrl = shortLinkService.getOriginUrl(code);
        log.info("短链code:{} -> 原链接:{}", code, originUrl);
        return new RedirectView(originUrl);
    }

    /**
     * 长转短
     *
     * @param request 用来获取host
     * @param url     原链接
     * @return 短链接
     */
    @PostMapping("/trans")
    public SystemJsonResponse transferAndSaveShortLink(HttpServletRequest request,
                                                       @RequestParam("url") @NotBlank @IsAccessible(message = "链接不可访问") String url) {
        // 拼接出基础的url
        String baseUrl = HttpServletUtil.getBaseUrl(request, "/api/v1/shortlink", "/{code}");
        // 转化
        String shortLinkURL = shortLinkService.transShortLinkURL(baseUrl, url);
        log.info("原链接:{} -> 短链接:{}", url, shortLinkURL);
        return SystemJsonResponse.SYSTEM_SUCCESS(shortLinkURL);
    }

    @PostMapping("/query")
    public SystemJsonResponse queryShortLinkList(@Valid @RequestBody(required = false) ShortLinkQueryDTO shortLinkQueryDTO) {
        ShortLinkQueryVO shortLinkQueryVO = shortLinkService.queryShortLinkList(shortLinkQueryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(shortLinkQueryVO);
    }

    @GetMapping("/remove/{id}")
    public SystemJsonResponse removeShortLink(@PathVariable("id") @NotNull Long id) {
        shortLinkService.removeShortLink(id);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
