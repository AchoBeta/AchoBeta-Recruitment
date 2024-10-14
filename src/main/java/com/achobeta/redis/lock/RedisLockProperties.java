package com.achobeta.redis.lock;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-13
 * Time: 0:34
 */
@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.redisson.lock")
public class RedisLockProperties {

    private Long wait;

    private Long timeout;

    private TimeUnit unit;

}
