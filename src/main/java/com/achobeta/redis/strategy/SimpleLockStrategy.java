package com.achobeta.redis.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-11
 * Time: 23:25
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SimpleLockStrategy implements LockStrategy {

    private final RedissonClient redisClient;

    @Override
    public RLock apply(String key) {
        log.info("普通锁 RLock {}", key);
        return redisClient.getLock(key);
    }
}
