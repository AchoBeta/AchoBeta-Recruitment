package com.achobeta.domain.login.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.login.model.dto.LoginDTO;
import com.achobeta.domain.login.service.strategy.LoginStrategy;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.util.ValidatorUtils;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 19:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    /**
     * # curl -X POST -H "Content-Type: application/json" -d '{"loginType": "email", "email": "sharksharkchen@qq.com", "emailCode": "433693"}' http://localhost:8080/api/v1/auth/login
     * POST http://localhost:8080/api/v1/auth/login
     * Content-Type: application/json
     *
     * {
     *   "loginType": "email",
     *   "email": "sharksharkchen@qq.com",
     *   "emailCode": "294283"
     * }
     * @param body
     * @return
     */
    @PostMapping("/login")
    public SystemJsonResponse login(@RequestBody String body) {
        LoginDTO loginBody = JSON.parseObject(body, LoginDTO.class);
        ValidatorUtils.validate(loginBody);

        String loginType = loginBody.getLoginType();
        LoginVO loginVO = LoginStrategy.doLogin(body, loginType);

        return SystemJsonResponse.SYSTEM_SUCCESS(loginVO);
    }
}
