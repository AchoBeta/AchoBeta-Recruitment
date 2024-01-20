package com.achobeta.domain.users.service.login;

import com.achobeta.common.constants.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cattleYuan
 * @date 2024/1/20
 */
@Component
public class LoginFactory {
    //注入所有登录策略
    @Autowired
    private List<LoginStrategy>  loginStrategyList;

    //通过登录枚举类选择登录策略
    public LoginStrategy getLoginStrategy(LoginType loginType){
        return loginStrategyList.stream().filter(data -> data.match(loginType)).findFirst().get();
    }

}
