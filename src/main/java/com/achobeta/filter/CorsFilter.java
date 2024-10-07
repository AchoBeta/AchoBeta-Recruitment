package com.achobeta.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "ab.request.cors")
public class CorsFilter implements Filter {

    private String allowOrigin;

    private String allowMethods;

    private String maxAge;

    private String allowHeaders;

    private String allowCredentials;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", allowOrigin); // 可以设置允许访问的域，也可以是具体的域名
        httpResponse.setHeader("Access-Control-Allow-Methods", allowMethods);
        httpResponse.setHeader("Access-Control-Max-Age", maxAge);
        httpResponse.setHeader("Access-Control-Allow-Headers", allowHeaders);
        httpResponse.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        chain.doFilter(request, response);
    }

}