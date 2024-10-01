package com.achobeta.handler;

import com.achobeta.common.annotation.handler.InterceptHelper;
import com.achobeta.config.RequestIdConfig;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-30
 * Time: 16:21
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpRequestLogHandler implements HandlerInterceptor {

    private final RequestIdConfig requestIdConfig;

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
                    log.warn("请求 {} 访问 {}，响应 HTTP 状态码 {}，错误信息 {}",
                            requestId, request.getRequestURI(), response.getStatus(),
                            Optional.ofNullable(ex).map(Exception::getMessage).orElse(null)
                    );
                }
            }
        } finally {
            BaseContext.removeCurrentUser();
        }
    }

}
