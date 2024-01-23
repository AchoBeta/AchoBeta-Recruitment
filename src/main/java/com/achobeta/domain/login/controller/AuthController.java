package com.achobeta.domain.login.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.login.model.dto.LoginDTO;
import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.domain.login.service.LoginService;
import com.achobeta.domain.login.service.strategy.LoginStrategy;
import com.achobeta.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 19:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginService loginService;

    /**
     * # curl -X POST -H "Content-Type: application/json" -d '{"loginType": "email", "email": "sharksharkchen@qq.com", "emailCode": "433693"}' http://localhost:8080/api/v1/auth/login
     * POST http://localhost:8080/api/v1/auth/login
     * Content-Type: application/json
     * <p>
     * {
     * "loginType": "email",
     * "email": "sharksharkchen@qq.com",
     * "emailCode": "294283"
     * }
     *
     * @param body
     * @return
     */
    @PostMapping("/login")
    public SystemJsonResponse login(@RequestBody Map<String, Object> body) {
        LoginDTO loginBody = BeanUtil.mapToBean(body, LoginDTO.class, false, new CopyOptions());
        ValidatorUtils.validate(loginBody);

        String loginType = loginBody.getLoginType();
        LoginVO loginVO = LoginStrategy.doLogin(body, loginType);

        return SystemJsonResponse.SYSTEM_SUCCESS(loginVO);
    }

    @PostMapping("register")
    public SystemJsonResponse register(@Validated @RequestBody RegisterDTO user) {
        loginService.register(user);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
