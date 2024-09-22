package com.achobeta.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:51
 */
public class HttpServletUtil {

    public static Optional<ServletRequestAttributes> getAttributes() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static Optional<HttpServletRequest> getRequest() {
        return getAttributes().map(ServletRequestAttributes::getRequest);
    }

    public static Optional<HttpServletResponse> getResponse() {
        return getAttributes().map(ServletRequestAttributes::getResponse);
    }

}
