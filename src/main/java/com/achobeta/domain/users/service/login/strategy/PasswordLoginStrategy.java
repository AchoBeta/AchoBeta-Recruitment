package com.achobeta.domain.users.service.login.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.achobeta.common.constants.LoginType;
import com.achobeta.domain.users.jwt.propertities.JwtProperties;
import com.achobeta.domain.users.model.vo.LoginVO;
import com.achobeta.domain.users.service.StudentService;
import com.achobeta.domain.users.service.login.LoginStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cattleYuan
 * @date 2024/1/20
 */
@Slf4j
@RequiredArgsConstructor
@Component("password")
public class PasswordLoginStrategy implements LoginStrategy {
    private final StudentService studentService;
    private final JwtProperties jwtProperties;
    @Override
    public boolean match(LoginType loginType) {
        return ObjectUtil.equal(loginType,LoginType.LOGINBYPASSWORD);
    }
    @Override
    public LoginVO login(String authenticationInfo) {
        //待完善
        log.info("密码登录！");
        return null;
    }
}
