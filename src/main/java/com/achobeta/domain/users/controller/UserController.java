package com.achobeta.domain.users.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 15:58
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    /**
     * http://localhost:9001/login?name=bantanger
     *
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
