package com.achobeta.config;

import jakarta.servlet.MultipartConfigElement;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 15:46
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "resource.upload")
public class ResourceUploadConfig {

    private Long maxFileSize;

    private Long maxRequestSize;

    /**
     * 配置上传文件大小的配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  设置单个文件大小
        factory.setMaxFileSize(DataSize.ofMegabytes(maxFileSize));
        // 设置总上传文件大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(maxRequestSize));
        // 设置临时存储位置（资源过大不直接写入内存），若进行设置清书写对应的异常处理器
//        factory.setFileSizeThreshold(DataSize.ofMegabytes());
//        factory.setLocation();
        return factory.createMultipartConfig();
    }

}
