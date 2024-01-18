package com.achobeta.domain.users.config;


import com.achobeta.domain.users.interpretor.UserInterpretor;
import com.achobeta.domain.users.propertities.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * 配置类，注册用户端；拦截器
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private UserInterpretor userInterpretor;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userInterpretor)
                .addPathPatterns("/api/v1/user/**")
                .excludePathPatterns("/api/v1/email/**");

    }


}
