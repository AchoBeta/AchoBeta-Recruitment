package com.achobeta.domain.shortlink.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.shortlink.service.ShortLinkService;
import com.achobeta.domain.shortlink.util.HttpUrlValidator;
import com.achobeta.domain.shortlink.util.ShortLinkUtils;
import com.achobeta.exception.IllegalUrlException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/shortlink")
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 重定向短链接
     * @param code 短链code
     * @return 重定向到原链接
     */
    @GetMapping("/{code}")
    public RedirectView getShortLink(@PathVariable("code")String code) {
        String originUrl = shortLinkService.getOriginUrl(code);
        return new RedirectView(originUrl);
    }

    /**
     * 长转短
     * @param request 用来获取host
     * @param url 原链接
     * @return 短链接
     */
    @PostMapping("/trans")
    public SystemJsonResponse transferAndSaveShortLink(HttpServletRequest request, @RequestParam("url")String url) {
        //验证url
        if(!HttpUrlValidator.isHttpUrl(url) || !HttpUrlValidator.isUrlAccessible(url)) {
            throw new IllegalUrlException(String.format("url:'%s' 无效", url));
        }
        // 拼接出基础的url
        String baseUrl = ShortLinkUtils.getBaseUrl(request.getHeader("host"));
        // 转化
        String shortLinkURL = shortLinkService.transShortLinkURL(baseUrl, url);
        log.info("原链接:{} -> 短链接:{}", url, shortLinkURL);
        return SystemJsonResponse.SYSTEM_SUCCESS(shortLinkURL);
    }
}
