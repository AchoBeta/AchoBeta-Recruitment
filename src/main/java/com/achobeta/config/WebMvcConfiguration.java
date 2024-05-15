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
        String[] studentIntercept = {
                "/api/v1/user/**",
                "/api/v1/questionnaire/**"
        };

        String[] managerIntercept = {
                "/api/v1/shortlink/**",
                "/api/v1/recruit/**",
                "/api/v1/period/**",
                "/api/v1/library/**",
                "/api/v1/paperentry/**",
                "/api/v1/question/entry/**",
                "/api/v1/question/paper/**"
        };

        registry.addInterceptor(userInterpretor)
                .addPathPatterns(studentIntercept)

                .addPathPatterns(managerIntercept)
                .excludePathPatterns("/api/v1/recruit/get/**")
                .excludePathPatterns("/api/v1/recruit/list/**")
                .excludePathPatterns("/api/v1/period/list/**")
        ;

        registry.addInterceptor(studentInterceptor)
                .addPathPatterns(studentIntercept)
                .excludePathPatterns("/api/v1/questionnaire/list/**")
        ;

        registry.addInterceptor(managerInterceptor)
                .addPathPatterns(managerIntercept)
                .excludePathPatterns("/api/v1/recruit/get/**")
                .excludePathPatterns("/api/v1/recruit/list/**")
                .excludePathPatterns("/api/v1/period/list/**")
                .addPathPatterns("/api/v1/questionnaire/list/**")
        ;

    }


}
