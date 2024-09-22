package com.achobeta.domain.resource.config;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ab.minio")
public class MinioConfig {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
