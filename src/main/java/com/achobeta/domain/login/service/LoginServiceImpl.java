package com.achobeta.domain.login.service;

import cn.hutool.crypto.digest.BCrypt;
import com.achobeta.common.enums.LoginTypeEnum;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.login.model.entity.LoginUser;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.interpretor.UserInterpretor;
import com.achobeta.jwt.propertities.JwtProperties;
import com.achobeta.jwt.util.JwtUtil;
import com.achobeta.redis.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.achobeta.common.enums.GlobalServiceStatusCode.USER_ACCOUNT_ALREADY_EXIST;
import static com.achobeta.redis.constants.RedisConstants.LOGIN_FAIL_CNT_KEY;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 16:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @Value("${ab.user.password.maxRetryCount:5}")
    private Integer maxRetryCount;

    @Value("${ab.user.password.lockTime:10}")
    private Integer lockTime;

    private final static Integer DEFAULT_ROLE = 1; // 非管理员

    private final RedisCache redisCache;
    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;

    @Override
    public UserEntity register(RegisterDTO registerBody) {
        String username = registerBody.getUsername();

        // TODO 验证码校验

        // 校验数据库中是否存在
        boolean exists = userMapper.exists(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username));

        if (exists) {
            String message = String.format("账号名称:'%s'已存在", username);
            throw new GlobalServiceException(message, USER_ACCOUNT_ALREADY_EXIST);
        }

        // 注册
        UserEntity user = registerUser(registerBody);
        log.info("账号名称:'{}'注册成功!", username);
        return user;
    }

    private UserEntity registerUser(RegisterDTO registerBody) {
        UserEntity user = new UserEntity();
        user.setUserType(DEFAULT_ROLE);
        user.setUsername(registerBody.getUsername());
        user.setNickname(registerBody.getUsername());
        String password = registerBody.getPassword();
        if(StringUtils.hasText(password)) {
            user.setPassword(BCrypt.hashpw(password));
        }
        user.setEmail(registerBody.getEmail());
        userMapper.insert(user);
        return user;
    }

    @Override
    public LoginVO login(LoginUser loginUser) {
        HashMap<String, Object> claims = new HashMap<>();
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getSecretKey());

        //将id存入claims
        if (Optional.ofNullable(loginUser.getUserId()).isPresent()) {
            claims.put(UserInterpretor.USER_ID, loginUser.getUserId());
            claims.put(UserTypeEnum.USER.getName(), loginUser.getUserType());
        }

        String token = JwtUtil.createJWT(secretKey, jwtProperties.getTtl(), claims);
        return LoginVO.builder()
                .accessToken(token)
                .expiresIn(jwtProperties.getTtl())
                .build();
    }

    @Override
    public void checkLogin(LoginTypeEnum loginTypeEnum, String username, Supplier<Boolean> supplier) {
        // 登录错误统一处理
        // 设置失败 key 为 key + username，限制，也可以是 key + username + ip 自定义限制
        String failKey = LOGIN_FAIL_CNT_KEY + username;

        // 获取用户登录失败次数
        int failCount = (int) redisCache.getCacheObject(failKey).orElse(0);

        // 锁定时间禁止登录
        if (failCount >= maxRetryCount) {
            String message = String.format("连续'%d'次登录失败, 请过'%d'分钟后再登录", maxRetryCount, lockTime);
            throw new GlobalServiceException(message, loginTypeEnum.getErrorCode());
        }

        if (supplier.get()) {
            // 失败次数递增
            // 不会引发线程安全问题，因为每个用户的 failCount 都是独立的，而单个用户也不会用多线程去执行这个方法
            failCount ++;
            // 用户登录失败次数缓存 ttl 设置为密码锁定周期
            // lockTime 默认单位是分钟，分钟转秒，秒转毫秒
            // 10 min = 10 * 60 * 1000 ms
            redisCache.setCacheObject(failKey, failCount, lockTime, TimeUnit.MINUTES); // 单位是 ms
            if (failCount >= maxRetryCount) {
                // 达到失败上限，锁定登录
                String message = String.format("连续'%d'次登录失败, 请过'%d'分钟后再登录", maxRetryCount, lockTime);
                throw new GlobalServiceException(message, loginTypeEnum.getErrorCode());
            } else {
                // 未达到失败上限，直接抛出异常
                String message = String.format("第'%d'次登录失败, 还剩'%d'次机会", failCount, maxRetryCount - failCount);
                throw new GlobalServiceException(message, loginTypeEnum.getErrorCode());
            }
        }

        // 登录成功，清除用户登录失败次数
        redisCache.deleteObject(failKey);
    }

    @Override
    public void logout() {

    }

}
