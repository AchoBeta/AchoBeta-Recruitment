package com.achobeta.interceptor;


import cn.hutool.core.util.StrUtil;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.annotation.handler.InterceptHelper;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.config.RequestIdConfig;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.jwt.propertities.JwtProperties;
import com.achobeta.jwt.util.JwtUtil;
import com.achobeta.util.HttpServletUtil;
import com.achobeta.util.SnowflakeIdGenerator;
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
import java.util.Optional;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

    public static final String USER_ID = "user_id";

    public static final String USER_ROLE_NAME = "user";

    private final RequestIdConfig requestIdConfig;

    private final JwtProperties jwtProperties;

    private final SnowflakeIdGenerator requestIdGenerator;

    private void refreshToken(HttpServletResponse response, SecretKey secretKey, Claims claims) {
        String refreshToken = JwtUtil.createJWT(secretKey, jwtProperties.getTtl(), claims);
        //刷新 token，通过请求头返回前端
        response.setHeader(jwtProperties.getTokenName(), refreshToken);
        log.info("无感刷新 token: {}", refreshToken);
    }

    public UserHelper getUserHelper(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        //从请求头中获取 token
        if (StrUtil.isEmpty(token)) {
            throw new GlobalServiceException("用户未登录,token为空", GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        //通过明文钥匙生成密钥
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getSecretKey());
        Claims claims = JwtUtil.parseJWT(secretKey, token);

        // 记录接口的访问记录
        UserHelper userHelper = UserHelper.builder()
                .userId(Long.parseLong(claims.get(UserInterceptor.USER_ID).toString()))
                .token(token)
                .role(Integer.parseInt(claims.get(UserInterceptor.USER_ROLE_NAME).toString()))
                .build();
        log.info("登录信息 -> {}", userHelper);

        //判断 token 是否即将过期
        if (JwtUtil.judgeApproachExpiration(token, secretKey, jwtProperties.getRefreshTime())) {
            refreshToken(HttpServletUtil.getResponse(), secretKey, claims);
        }
        //通过线程局部变量设置当前线程用户信息
        BaseContext.setCurrentUser(userHelper);
        return userHelper;
    }

    public UserHelper getUserHelper() {
        return Optional.ofNullable(BaseContext.getCurrentUser())
                .orElseGet(() -> getUserHelper(HttpServletUtil.getRequest()));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //可以解决拦截器跨域问题
        if (!(handler instanceof HandlerMethod)) {
            // 并不处理非目标方法的请求
            // todo: 例如通过本服务，但不是通过目标方法获取资源的请求，而这些请求需要进行其他的处理！
            return Boolean.TRUE;
        }
        // 设置请求 id
        long requestId = requestIdGenerator.nextId();
        response.setHeader(requestIdConfig.getHeader(), String.valueOf(requestId));
        // 获取目标方法
        Method targetMethod = ((HandlerMethod) handler).getMethod();
        // 获取 intercept 注解实例
        Intercept intercept = InterceptHelper.getIntercept(targetMethod);
        // 判断是否忽略
        if(InterceptHelper.isIgnore(intercept)) {
            return Boolean.TRUE;
        }

        UserHelper userHelper = getUserHelper(request);
        // 记录接口的访问记录
        log.info("请求 {} 账户 {} 访问接口 {} ", requestId, userHelper.getUserId(), request.getRequestURI());
        // permit 中没有 role 就会抛异常
        Integer role = userHelper.getRole();
        if(!InterceptHelper.isValid(intercept, UserTypeEnum.get(role))) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }

        return Boolean.TRUE;
    }

    // 此方法在全局响应异常处理器处理异常之后再执行，已经是大局已定了，对响应的写不会生效，抛出的异常也不会影响正常响应
    // 因为请求还没结束，这个方法的处理时间也在请求时间内，会影响响应速度
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            if(handler instanceof HandlerMethod handlerMethod) {
                // 获取目标方法
                Method targetMethod = handlerMethod.getMethod();
                if(InterceptHelper.shouldPrintLog(targetMethod)) {
                    String requestId = response.getHeader(requestIdConfig.getHeader());
                    log.warn("请求 {} 访问接口 {}，响应 HTTP 状态码 {}，错误信息 {}",
                            requestId, request.getRequestURI(), response.getStatus(),
                            Optional.ofNullable(ex).map(Exception::getMessage).orElse(null)
                    );
                }
            }
        } finally {
            log.info("删除本地线程变量");
            BaseContext.removeCurrentUser();
        }
    }

}
