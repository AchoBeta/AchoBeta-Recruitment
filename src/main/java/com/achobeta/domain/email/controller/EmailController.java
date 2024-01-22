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


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
public class EmailController {
    //    private final LoginLauncher loginLauncher;
    private final EmailService emailService;

    /**
     * POST http://localhost:8080/api/v1/resource/email/code?email=sharksharkchan@qq.com
     * 发送验证码接口
     *
     * @param email
     * @return
     */
    @PostMapping("/email/code")
    public SystemJsonResponse emailIdentityCheck(@RequestParam("email") @Email String email) {
        // 获得随机验证码
        String code = IdentifyingCodeValidator.getIdentifyingCode();
        emailService.sendIdentifyingCode(email, code);
        // 能到这一步就成功了
        log.info("发送验证码:{} -> email:{}", code, email);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
