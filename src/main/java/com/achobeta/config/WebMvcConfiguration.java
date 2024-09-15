package com.achobeta.config;


import com.achobeta.interpretor.UserInterpretor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 *  @author cattleYuan
 *  @date 2024/1/18
 */
//配置类，注册用户端；拦截器
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final UserInterpretor userInterpretor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterpretor)
                .addPathPatterns("/**");

    }

}
