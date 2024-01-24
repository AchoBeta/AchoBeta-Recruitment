package com.achobeta.domain.email.controller;

import cn.hutool.core.util.RandomUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.email.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
public class EmailController {

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
        // 生成 6位数 随机验证码
        String code = RandomUtil.randomNumbers(6);
        emailService.sendIdentifyingCode(email, code);
        // 能到这一步就成功了
        log.info("发送验证码:{} -> email:{}", code, email);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
