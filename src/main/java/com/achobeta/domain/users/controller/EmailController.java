package com.achobeta.domain.users.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.email.component.EmailValidator;
import com.achobeta.domain.users.service.EmailService;
import com.achobeta.domain.users.util.IdentifyingCodeValidator;
import com.achobeta.exception.IllegalEmailException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    /**
     * 发送验证码接口
     * @param email
     * @return
     */
    @PostMapping("/check")
    public SystemJsonResponse emailIdentityCheck(@RequestParam("email") @NonNull String email) {
        if(!EmailValidator.isEmailAccessible(email)) {
            throw new IllegalEmailException("邮箱格式错误");
        }
        // 获得随机数
        String code = IdentifyingCodeValidator.getIdentifyingCode();
        emailService.sendIdentifyingCode(email, code);
        // 能到这一步就成功了
        log.info("发送验证码:{} -> email:{}", code, email);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/check/{code}")
    public SystemJsonResponse checkCode(@RequestParam("email") @NonNull String email,
                                        @PathVariable("code") @NonNull String code) {
        if(!EmailValidator.isEmailAccessible(email)) {
            throw new IllegalEmailException("邮箱格式错误");
        }
        emailService.checkIdentifyingCode(email, code);
        // 成功
        log.info("email:{}, 验证码:{} 验证成功", email, code);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
