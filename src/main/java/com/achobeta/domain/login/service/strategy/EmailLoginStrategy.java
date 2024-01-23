package com.achobeta.domain.login.service.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.LoginTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.login.model.dto.EmailLoginDTO;
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

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.achobeta.common.constants.RedisConstants.CAPTCHA_CODES_KEY;
import static com.achobeta.common.constants.RedisConstants.CAPTCHA_CODE_KEY;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:44
 */
@Slf4j
@Service("email" + LoginStrategy.BASE_NAME)
@RequiredArgsConstructor
public class EmailLoginStrategy implements LoginStrategy {

    private final UserMapper userMapper;
    private final RedisCache redisCache;
    private final LoginService loginService;

    @Override
    public LoginVO doLogin(Map<String, Object> body) {
        EmailLoginDTO loginBody = BeanUtil.mapToBean(body, EmailLoginDTO.class, false);
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

    private LoginUser buildLoginUser(UserEntity user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId() + user.getUuid());
        loginUser.setUsername(user.getUsername());
        loginUser.setEmail(user.getEmail());
        loginUser.setPhoneNumber(user.getPhoneNumber());
        loginUser.setUserType(user.getUserType());
        loginUser.setOpenid(user.getId() + user.getUuid());
        return loginUser;
    }

    private UserEntity findOrCreateUserByEmail(String email) {
        // 查询是否存在该邮箱
        UserEntity user = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getEmail, email));

        if (null == user) {
            // 不存在邮箱走注册逻辑
            user = new UserEntity();
            user.setUsername(email);
            user.setEmail(email);
            // 生成 32 位 uuid 防重
            user.setUuid((UUID.randomUUID().toString().replace("-", "")).substring(0, 32));
            userMapper.insert(user);
            log.info("用户 email: '{}' 新建完成", email);
        }
        return user;
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
