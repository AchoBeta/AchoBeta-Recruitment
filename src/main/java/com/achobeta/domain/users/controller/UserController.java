package com.achobeta.domain.users.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 15:58
 */
@RestController
public class UserController {

    /**
     * http://localhost:9001/login?name=bantanger
     * 
     * @param name
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam("name") String name) {
        return name + " 登录成功123";
    }

}
