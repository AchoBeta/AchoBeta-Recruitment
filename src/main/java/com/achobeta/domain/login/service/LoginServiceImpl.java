package com.achobeta.domain.login.service;

import com.achobeta.common.constants.RedisConstants;
import com.achobeta.common.enums.LoginTypeEnum;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.jwt.propertities.JwtProperties;
import com.achobeta.jwt.util.JwtUtil;
import com.achobeta.domain.login.model.entity.LoginUser;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.interpretor.UserInterpretor;
import com.achobeta.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

import static com.achobeta.common.constants.RedisConstants.LOGIN_FAIL_CNT_KEY;

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

    private final RedisCache redisCache;
    private final JwtProperties jwtProperties;

    @Override
    public void register() {

    }

    @Override
    public LoginVO login(LoginUser loginUser) {
        HashMap<String, Object> claims = new HashMap<>();
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getSecretKey());

        //将id存入claims
        if (Optional.ofNullable(loginUser.getUserId()).isPresent()) {
            claims.put(UserInterpretor.USER_ID, loginUser.getUserId());
            claims.put(UserTypeEnum.USER.getName(), loginUser.getUsername());
        }

        String token = JwtUtil.createJWT(secretKey, jwtProperties.getTtl(), claims);
        return LoginVO.builder()
                .openid(loginUser.getOpenid())
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
            redisCache.setCacheObject(failKey, failCount, lockTime * 60 * 1000L); // 单位是 ms
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
