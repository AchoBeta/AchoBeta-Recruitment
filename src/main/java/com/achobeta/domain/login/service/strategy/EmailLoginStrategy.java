package com.achobeta.domain.login.service.strategy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.LoginTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.login.model.dto.EmailLoginDTO;
import com.achobeta.domain.login.model.dto.LoginDTO;
import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.login.model.entity.LoginUser;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.domain.login.service.LoginService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.RedisCache;
import com.achobeta.util.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.achobeta.common.constants.RedisConstants.CAPTCHA_CODES_KEY;
import static com.achobeta.common.constants.RedisConstants.CAPTCHA_CODE_KEY;
import static com.achobeta.common.enums.GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:44
 */
@Slf4j
@Service("email" + LoginStrategy.BASE_NAME)
@RequiredArgsConstructor
public class EmailLoginStrategy implements LoginStrategy {

    private final RedisCache redisCache;
    private final UserMapper userMapper;
    private final LoginService loginService;

    @Override
    public LoginVO doLogin(LoginDTO body) {
        if (LoginTypeEnum.EMAIL.getMessage().equals(body.getLoginType()) && body.getEmailParams() == null) {
            String message = String.format("'%s'参数为空，原请求参数为:'%s'", body.getLoginType(), body);
            throw new GlobalServiceException(message, SYSTEM_SERVICE_FAIL);
        }
        EmailLoginDTO loginBody = body.getEmailParams();
        ValidatorUtils.validate(loginBody);

        String email = loginBody.getEmail();
        String emailCode = loginBody.getEmailCode();

        // 通过邮箱查找用户
        UserEntity user = findOrCreateUserByEmail(email);

        // 检查是否能登录
        loginService.checkLogin(LoginTypeEnum.EMAIL, user.getUsername(),
                () -> !validateEmailCode(email, emailCode));

        // TODO
//        // 自定义分配，不同用户有不同 token 授权时间，不设置默认走全局
//        LoginModel loginModel = new LoginModel();
//        loginModel.setTimeout();

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

    private UserEntity findOrCreateUserByEmail(String email) {
        // 查询是否存在该邮箱
        UserEntity user = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getEmail, email));
        return Optional.ofNullable(user).orElseGet(() -> {
            // 不存在邮箱走注册逻辑
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setEmail(email);
            registerDTO.setUsername(email);
            return loginService.register(registerDTO);
        });
    }

    private boolean validateEmailCode(String email, String emailCode) {
        Optional<String> codeOptional = redisCache.getCacheMapValue(CAPTCHA_CODES_KEY + email, CAPTCHA_CODE_KEY);
        String code = codeOptional.orElseThrow(() -> {
            String message = String.format("Redis 中不存在邮箱[%s]的相关记录", email);
            return new GlobalServiceException(message, GlobalServiceStatusCode.EMAIL_NOT_EXIST_RECORD);
        });
        return code.equals(emailCode);
    }

}
