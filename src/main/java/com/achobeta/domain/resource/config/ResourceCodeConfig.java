package com.achobeta.domain.resource.config;

import com.achobeta.util.SnowflakeIdGenerator;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 1:20
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "resource.code")
public class ResourceCodeConfig {

    private Long workerId;

    @Bean
    public SnowflakeIdGenerator resourceCodeGenerator() {
        return new SnowflakeIdGenerator(workerId);
    }

}
