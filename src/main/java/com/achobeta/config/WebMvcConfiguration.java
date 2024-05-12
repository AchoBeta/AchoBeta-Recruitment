package com.achobeta.config;


import com.achobeta.interpretor.ManagerInterceptor;
import com.achobeta.interpretor.StudentInterceptor;
import com.achobeta.interpretor.UserInterpretor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 *  @author cattleYuan
 *  @date 2024/1/18
 */
//配置类，注册用户端；拦截器
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    private final UserInterpretor userInterpretor;

    private final ManagerInterceptor managerInterceptor;

    private final StudentInterceptor studentInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userInterpretor)
                .addPathPatterns("/api/v1/user/**")
                .addPathPatterns("/api/v1/questionnaire/**")

                .addPathPatterns("/api/v1/shortlink/**")
                .addPathPatterns("/api/v1/recruit/**")
                .excludePathPatterns("/api/v1/recruit/get/**")
                .addPathPatterns("/api/v1/period/**")
                .excludePathPatterns("/api/v1/period/list/**")
                .addPathPatterns("/api/v1/entry/**")
                .excludePathPatterns("/api/v1/entry/list/**")
        ;

        registry.addInterceptor(studentInterceptor)
                .addPathPatterns("/api/v1/user/**")
                .addPathPatterns("/api/v1/questionnaire/**")
                .excludePathPatterns("/api/v1/questionnaire/list/**")
        ;

        registry.addInterceptor(managerInterceptor)
                .addPathPatterns("/api/v1/shortlink/**")
                .addPathPatterns("/api/v1/recruit/**")
                .excludePathPatterns("/api/v1/recruit/get/**")
                .addPathPatterns("/api/v1/period/**")
                .excludePathPatterns("/api/v1/period/list/**")
                .addPathPatterns("/api/v1/entry/**")
                .excludePathPatterns("/api/v1/entry/list/**")
                .addPathPatterns("/api/v1/questionnaire/list/**")
        ;

    }


}
