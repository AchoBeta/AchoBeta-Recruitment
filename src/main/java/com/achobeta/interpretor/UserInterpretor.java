package com.achobeta.interpretor;


import cn.hutool.core.util.StrUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserType;
import com.achobeta.domain.login.service.LoginService;
import com.achobeta.domain.users.context.BaseContext;


import com.achobeta.jwt.propertities.JwtProperties;
import com.achobeta.jwt.util.JwtUtil;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.domain.users.service.StuResumeService;
import com.achobeta.exception.GlobalServiceException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInterpretor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    public static final String USER_ID = "user_id";
    public static final String UserRoleName = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //可以解决拦截器跨域问题
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //从请求头中获取token
        if (StrUtil.isEmpty(token)) {
            throw new GlobalServiceException("用户未登录,token为空", GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        //通过明文钥匙生成密钥
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getUserSecretKey());

        io.jsonwebtoken.Claims claims = JwtUtil.parseJWT(secretKey, token);
        //通过线程局部变量设置当前线程用户信息
        setGlobaleUserInfoByClaims(claims, token);
        //判断token是否即将过期
        if (JwtUtil.judgeApproachExpiration(token, secretKey)) {
            refreshToken(response, secretKey, claims);
        }
        return true;
    }

    private void refreshToken(HttpServletResponse response, SecretKey secretKey, Claims claims) {
        long ttl = jwtProperties.getUserTtl();
        String refreshToken = JwtUtil.createJWT(secretKey, ttl, claims);
        //刷新token,通过请求头返回前端
        response.setHeader(jwtProperties.getUserTokenName(), refreshToken);
        log.info("无感刷新token:{}", refreshToken);
    }

    private void setGlobaleUserInfoByClaims(Claims claims, String token) {
        Long userId = Long.valueOf(claims.get(UserInterpretor.USER_ID).toString());
        Integer role = Integer.parseInt(claims.get(UserType.USER.getName()).toString());
        UserHelper userHelper = UserHelper.builder()
                .userId(userId)
                .token(token)
                .role(role)
                .build();
        log.info("登录信息->{}",userHelper);
        BaseContext.setCurrentUser(userHelper);
    }
}
