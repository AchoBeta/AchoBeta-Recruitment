package com.achobeta.interpretor;


import cn.hutool.core.util.StrUtil;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.users.context.BaseContext;


import com.achobeta.common.annotation.handler.InterceptHelper;
import com.achobeta.jwt.propertities.JwtProperties;
import com.achobeta.jwt.util.JwtUtil;
import com.achobeta.domain.users.model.po.UserHelper;
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
import java.lang.reflect.Method;

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
    public static final String USER_ROLE_NAME = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //可以解决拦截器跨域问题
        if (!(handler instanceof HandlerMethod)) {
            // 并不处理非目标方法的请求
            // todo: 例如获取资源的请求，而这些请求需要进行其他的处理！
            return true;
        }

        // 获取目标方法
        Method targetMethod = ((HandlerMethod) handler).getMethod();
        // 获取 intercept 注解实例
        Intercept intercept = InterceptHelper.getIntercept(targetMethod);
        // 判断是否忽略
        if(InterceptHelper.isIgnore(intercept)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getTokenName());
        //从请求头中获取token
        if (StrUtil.isEmpty(token)) {
            throw new GlobalServiceException("用户未登录,token为空", GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        //通过明文钥匙生成密钥
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getSecretKey());

        Claims claims = JwtUtil.parseJWT(secretKey, token);

        // permit 中没有 role 就会抛异常
        UserTypeEnum role = UserTypeEnum.get(Integer.parseInt(claims.get(UserInterpretor.USER_ROLE_NAME).toString()));
        if(!InterceptHelper.isValid(intercept, role)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }

        //通过线程局部变量设置当前线程用户信息
        setGlobalUserInfoByClaims(claims, token);
        //判断token是否即将过期
        if (JwtUtil.judgeApproachExpiration(token, secretKey)) {
            refreshToken(response, secretKey, claims);
        }
        return true;
    }

    private void refreshToken(HttpServletResponse response, SecretKey secretKey, Claims claims) {
        long ttl = jwtProperties.getTtl();
        String refreshToken = JwtUtil.createJWT(secretKey, ttl, claims);
        //刷新token,通过请求头返回前端
        response.setHeader(jwtProperties.getTokenName(), refreshToken);
        log.info("无感刷新token:{}", refreshToken);
    }

    private void setGlobalUserInfoByClaims(Claims claims, String token) {
        Long userId = Long.valueOf(claims.get(UserInterpretor.USER_ID).toString());
        Integer role = Integer.parseInt(claims.get(UserInterpretor.USER_ROLE_NAME).toString());
        UserHelper userHelper = UserHelper.builder()
                .userId(userId)
                .token(token)
                .role(role)
                .build();
        log.info("登录信息->{}",userHelper);
        BaseContext.setCurrentUser(userHelper);
    }
}
