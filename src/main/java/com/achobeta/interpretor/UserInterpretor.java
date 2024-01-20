package com.achobeta.interpretor;


import cn.hutool.core.util.StrUtil;
import com.achobeta.common.constants.GlobalServiceStatusCode;
import com.achobeta.domain.users.context.BaseContext;


import com.achobeta.domain.users.jwt.propertities.JwtProperties;
import com.achobeta.domain.users.jwt.util.JwtUtil;
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

    public static final String USER_ID="user_id";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //可以解决拦截器跨域问题
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //从请求头中获取token
        if(StrUtil.isEmpty(token)){
            throw new GlobalServiceException("用户未登录,token为空", GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        //通过明文钥匙生成密钥
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getUserSecretKey());

        io.jsonwebtoken.Claims claims = JwtUtil.parseJWT(secretKey, token);
        //通过当前线程类对象设置当前线程用户id
        setGlobaleUserIdByClaims(claims);
        //判断token是否即将过期
        if(JwtUtil.judgeApproachExpiration(token,secretKey)){
            refreshToken(response, secretKey, claims);
        }
        return true;
    }

    private void refreshToken(HttpServletResponse response, SecretKey secretKey, Claims claims) {
        long ttl = jwtProperties.getUserTtl();
        String refreshToken = JwtUtil.createJWT(secretKey, ttl, claims);
        //刷新token,通过请求头返回前端
        response.setHeader(jwtProperties.getUserTokenName(),refreshToken);
        log.info("无感刷新token:{}",refreshToken);
    }

    private void setGlobaleUserIdByClaims(Claims claims) {
        Long userId = Long.valueOf(claims.get(UserInterpretor.USER_ID).toString());
        BaseContext.setCurrentId(userId);
    }
}