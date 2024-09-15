package com.achobeta.domain.login.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.domain.login.model.dto.LoginDTO;
import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.domain.login.service.LoginService;
import com.achobeta.domain.login.service.strategy.LoginStrategy;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.achobeta.domain.login.service.strategy.LoginStrategy.BASE_NAME;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 19:24
 */
@Slf4j
@Validated
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
     * @param loginBody
     * @return
     */
    @PostMapping("/login")
    @Intercept(ignore = true)
    public SystemJsonResponse login(@Valid @RequestBody LoginDTO loginBody) {
        String beanName = loginBody.getLoginType() + BASE_NAME;
        ListableBeanFactory beanFactory = SpringUtil.getBeanFactory();
        if (!beanFactory.containsBean(beanName)) {
            throw new GlobalServiceException(String.format("ioc 容器未找到 bean:'%s'", beanName));
        }
        LoginStrategy instance = (LoginStrategy) beanFactory.getBean(beanName);
        LoginVO loginVO = instance.doLogin(loginBody);

        return SystemJsonResponse.SYSTEM_SUCCESS(loginVO);
    }

    /**
     * ###
     * POST http://localhost:8080/api/v1/auth/register
     * Content-Type: application/json
     *
     * {
     *   "username": "1290288968",
     *   "password": "123456"
     * }
     * @param user
     * @return
     */
    @PostMapping("/register")
    @Intercept(ignore = true)
    public SystemJsonResponse register(@Valid @RequestBody RegisterDTO user) {
        user.setEmail(null);
        loginService.register(user);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
