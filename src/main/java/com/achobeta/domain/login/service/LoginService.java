package com.achobeta.domain.login.service;

import com.achobeta.domain.login.enums.LoginTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.login.model.entity.LoginUser;
import com.achobeta.domain.login.model.vo.LoginVO;

import java.util.function.Supplier;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 16:44
 */
public interface LoginService {

    /**
     * 注册
     */
    UserEntity register(RegisterDTO registerBody);

    LoginVO login(LoginUser loginUser);

    /**
     * 登录校验
     */
    void checkLogin(LoginTypeEnum loginTypeEnum, String username, Supplier<Boolean> supplier);

    /**
     * 退出登录
     */
    void logout();

}
