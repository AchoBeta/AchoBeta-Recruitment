package com.achobeta.domain.templateengine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-09-07
 * Time: 16:20
 * Description: 此类不限制是不是 HTML、TEXT、XML、JAVASCRIPT、CSS、RAW 类型的模板，其他形式的文件均认为是 TEXT，并遵循规则插入
 */
@Component
@RequiredArgsConstructor
public class UnsafeTemplateEngine extends SpringTemplateEngine implements InitializingBean {

    private final static String CLASS_TEMPLATE_PATH = "classpath:/templates/";

    private final ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
        springResourceTemplateResolver.setApplicationContext(applicationContext);
        springResourceTemplateResolver.setPrefix(CLASS_TEMPLATE_PATH);
        setTemplateResolver(springResourceTemplateResolver);
    }
}
