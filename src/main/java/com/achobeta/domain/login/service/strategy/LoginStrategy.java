package com.achobeta.domain.login.service.strategy;

import com.achobeta.domain.login.model.dto.LoginDTO;
import com.achobeta.domain.login.model.vo.LoginVO;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:39
 */
public interface LoginStrategy {

    String BASE_NAME = "LoginStrategy";

    /**
     * 登录
     * @param loginBody
     * @return
     */
    LoginVO doLogin(LoginDTO loginBody);

}
