package com.achobeta.domain.resource.config;

import com.achobeta.domain.resource.service.ObjectStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 10:49
 */
@Configuration
public class ObjectStorageServiceConfig {

    @Bean
    public ObjectStorageService objectStorageService() {
        // 此对象通过 spi 加载后通过 @Bean 放入 Spring 容器，已经构造过了，所以不能通过构造方法注入，只能属性注入！
        return ServiceLoader.load(ObjectStorageService.class).findFirst().orElse(null);
    }

}
