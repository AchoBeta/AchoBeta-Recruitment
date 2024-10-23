package com.achobeta.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "ab.request.cors")
public class CorsFilter implements Filter {

    private String allowOrigin;

    private Set<String> allowOriginSet;

    private String allowMethods;

    private String maxAge;

    private String allowHeaders;

    private String allowCredentials;

    @PostConstruct
    public void doPostConstruct() {
        allowOriginSet = new HashSet<>();
        Optional.ofNullable(allowOrigin)
                .stream()
                .filter(StringUtils::hasText)
                .map(origins -> origins.split(","))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .forEach(allowOriginSet::add);
        log.info("允许跨域的请求源：{}", allowOriginSet);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 获取请求源
        String origin = httpRequest.getHeader("Origin");
        if(StringUtils.hasText(origin) && allowOrigin.contains(origin)) {
            // 可以设置允许访问的域，也可以是具体的域名
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        }
        httpResponse.setHeader("Access-Control-Allow-Methods", allowMethods);
        httpResponse.setHeader("Access-Control-Max-Age", maxAge);
        httpResponse.setHeader("Access-Control-Allow-Headers", allowHeaders);
        httpResponse.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        chain.doFilter(request, response);
    }

}