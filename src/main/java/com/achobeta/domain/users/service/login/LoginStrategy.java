package com.achobeta.domain.users.service.login;

import com.achobeta.common.constants.LoginType;
import com.achobeta.domain.users.model.vo.LoginVO;
import org.springframework.stereotype.Service;


public interface LoginStrategy {
    //用这个替代if-else语句
    boolean match(LoginType loginType);
    LoginVO login(String authenticationInfo);
}
