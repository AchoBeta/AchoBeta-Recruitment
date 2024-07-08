package com.achobeta.domain.login.service.strategy;

import cn.hutool.crypto.digest.BCrypt;
import com.achobeta.common.enums.LoginTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.login.model.dto.LoginDTO;
import com.achobeta.domain.login.model.dto.PasswordLoginDTO;
import com.achobeta.domain.login.model.entity.LoginUser;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.domain.login.service.LoginService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.achobeta.common.enums.GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL;
import static com.achobeta.common.enums.GlobalServiceStatusCode.USER_ACCOUNT_NOT_EXIST;
import static com.achobeta.domain.login.service.strategy.LoginStrategy.BASE_NAME;

/**
 * @author BanTanger 半糖
 * @date 2024/1/23 21:04
 */
@Slf4j
@Service("password" + BASE_NAME)
@RequiredArgsConstructor
public class PasswordLoginStrategy implements LoginStrategy {

    private final UserMapper userMapper;
    private final LoginService loginService;

    @Override
    public LoginVO doLogin(LoginDTO body) {
        if (LoginTypeEnum.PASSWORD.getMessage().equals(body.getLoginType()) && body.getPasswordParams() == null) {
            String message = String.format("'%s'参数为空，原请求参数为:'%s'", body.getLoginType(), body);
            throw new GlobalServiceException(message, SYSTEM_SERVICE_FAIL);
        }
        PasswordLoginDTO loginBody = body.getPasswordParams();
        ValidatorUtils.validate(loginBody);

        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        // 查找数据库是否存在该用户信息
        UserEntity user = findUserByUsername(username);

        // 检查是否能登录
        loginService.checkLogin(LoginTypeEnum.PASSWORD, user.getUsername(),
                () -> !BCrypt.checkpw(password, user.getPassword()));

        return loginService.login(buildLoginUser(user));
    }

    /**
     * 数据脱敏处理
     *
     * @param user
     * @return
     */
    private LoginUser buildLoginUser(UserEntity user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(user.getUsername());
        loginUser.setUserType(user.getUserType());
        loginUser.setUserId(user.getId());
        return loginUser;
    }

    private UserEntity findUserByUsername(String username) {
        UserEntity user = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username));

        Optional.ofNullable(user).filter(u -> u.getUsername() != null).orElseThrow(() -> {
            String message = String.format("登录用户:'%s'不存在", username);
            return new GlobalServiceException(message, USER_ACCOUNT_NOT_EXIST);
        });
        return user;
    }

}
