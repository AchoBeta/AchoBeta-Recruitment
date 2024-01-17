package com.achobeta.domain.users.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.email.component.EmailValidator;
import com.achobeta.domain.users.service.EmailService;
import com.achobeta.domain.users.service.UserService;
import com.achobeta.domain.users.util.IdentifyingCodeValidator;
import com.achobeta.exception.IllegalEmailException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 15:58
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

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

    /**
     * http://localhost:9001/login?name=bantanger
     * @param name
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam("name") String name) {
        return name + " 登录成功";
    }

    @GetMapping("/exception")
    public void test() {
        userService.test_NotPermissionExceptionHandler();
    }

    @GetMapping("/mock/test")
    public SystemJsonResponse mockTest() {
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
