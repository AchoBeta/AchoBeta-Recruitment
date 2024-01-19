package com.achobeta.domain.users.interpretor;


import cn.hutool.core.util.StrUtil;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.jwt.util.propertities.JwtProperties;

import com.achobeta.domain.users.jwt.util.JwtUtil;
import com.achobeta.exception.GlobalServiceException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserInterpretor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    private static final String USER_ID="user_id";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断跨域请求
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //从请求头中获取token
        if(StrUtil.isEmpty(token)){
            throw new GlobalServiceException("用户未登录");
        }
        //通过明文钥匙生成密钥
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getUserSecretKey());

        io.jsonwebtoken.Claims claims = JwtUtil.parseJWT(secretKey, token);
        setGlobaleUserIdByClaims(claims);
        //判断token是否即将过期
        if(JwtUtil.judgeApproachExpiration(token,secretKey)){
            long ttl = jwtProperties.getUserTtl();
            //刷新token,通过请求头返回前端
            response.setHeader(jwtProperties.getUserTokenName(),JwtUtil.createJWT(secretKey,ttl,claims));
        }
        return true;
    }

    private void setGlobaleUserIdByClaims(Claims claims) {
        Long userId = Long.valueOf(claims.get(jwtProperties.getUserTokenName()).toString());
        BaseContext.setCurrentId(userId);
    }
}
