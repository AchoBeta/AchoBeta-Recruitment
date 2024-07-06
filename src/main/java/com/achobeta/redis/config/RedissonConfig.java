package com.achobeta.redis.config;

import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-04-03
 * Time: 10:55
 */
@Configuration
@Setter
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonConfig {

    private String url;

    private String password;

    private Integer database;

    @Bean()
    public RedissonClient redisson() {
        Config config = new Config();
        // 配置 Redisson 连接信息
        config.useSingleServer()
                .setAddress(url)
                .setDatabase(database) // 设置数据库索引
                .setPassword(password) // 设置密码（如果需要）
        ;
        return Redisson.create(config);
    }


}
