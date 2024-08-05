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

        String[] noneIntercept = {
                "/api/v1/auth/**",
                "/api/v1/resource/email/code/**",
                "/api/v1/shortlink/*",
        };

        String[] studentOrManagerIntercept = {
            "/api/v1/recruit/activity/template/**",
        };

        String[] studentIntercept = {
                "/api/v1/resume/submit/**",

                "/api/v1/participate/get/**",
                "/api/v1/participate/submit/**",

                "/api/v1/recruit/activity/list/user/**",

                "/api/v1/recruit/batch/list/user/**",
        };

        String[] managerIntercept = {
                "/api/v1/library/question/**",
                "/api/v1/library/paper/**",
                "/api/v1/question/**",
                "/api/v1/qpaper/**",
                "/api/v1/pqlink/**",

                "/api/v1/period/**",

                "/api/v1/recruit/activity/create/**",
                "/api/v1/recruit/activity/shift/**",
                "/api/v1/recruit/activity/update/**",
                "/api/v1/recruit/activity/set/paper/**",
                "/api/v1/recruit/activity/list/manager/**",

                "/api/v1/recruit/batch/create/**",
                "/api/v1/recruit/batch/update/**",
                "/api/v1/recruit/batch/list/manager/**",
                "/api/v1/recruit/batch/participants/**",
                "/api/v1/recruit/batch/shift/**",

                "/api/v1/shortlink/trans/**",

                "/api/v1/schedule/**",
        };

        registry.addInterceptor(userInterpretor)
                .addPathPatterns(studentOrManagerIntercept)
                .addPathPatterns(studentIntercept)
                .addPathPatterns(managerIntercept)
        ;

        registry.addInterceptor(studentInterceptor)
                .addPathPatterns(studentIntercept)
        ;

        registry.addInterceptor(managerInterceptor)
                .addPathPatterns(managerIntercept)
        ;

    }


}
