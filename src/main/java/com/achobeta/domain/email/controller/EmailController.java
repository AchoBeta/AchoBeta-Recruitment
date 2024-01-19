package com.achobeta.domain.email.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.email.service.EmailService;
import com.achobeta.domain.email.util.IdentifyingCodeValidator;
import jakarta.validation.constraints.Email;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    /**
     * 发送验证码接口
     *
     * @param email
     * @return
     */
    @PostMapping("/check")
    public SystemJsonResponse emailIdentityCheck(@RequestParam("email") @Email String email) {
        // 获得随机验证码
        String code = IdentifyingCodeValidator.getIdentifyingCode();
        emailService.sendIdentifyingCode(email, code);
        // 能到这一步就成功了
        log.info("发送验证码:{} -> email:{}", code, email);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/check/{code}")
    public SystemJsonResponse checkCode(@RequestParam("email") @Email String email,
                                        @PathVariable("code") @NonNull String code) {
        // 验证
        emailService.checkIdentifyingCode(email, code);
        // 成功
        log.info("email:{}, 验证码:{} 验证成功", email, code);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
