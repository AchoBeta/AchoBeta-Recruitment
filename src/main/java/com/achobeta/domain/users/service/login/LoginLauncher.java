package com.achobeta.domain.users.service.login;

import com.achobeta.common.constants.LoginType;
import com.achobeta.domain.users.model.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author cattleYuan
 * @date 2024/1/20
 */
//登录选择
@Component
@RequiredArgsConstructor
public class LoginLauncher {
    private final LoginFactory loginFactory;

    public LoginVO login(LoginType loginType,String authenticationInfo){
        //获取登录策略
        LoginStrategy loginStrategy = loginFactory.getLoginStrategy(loginType);
        return loginStrategy.login(authenticationInfo);
    }
}
