package com.achobeta.domain.shortlink.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCache {

    private final RedisTemplate redisTemplate;

    private final RedisBloomFilter redisBloomFilter;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        log.info("存入Redis\t[{}]-[{}]", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> T getCacheObject(final String key) {
        T value = (T) redisTemplate.opsForValue().get(key);
        log.info("查询Redis\t[{}]-[{}]", key, value);
        return value;
    }
 
    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        log.info("删除Redis键值\tkey[{}]", key);
        return redisTemplate.delete(key);
    }

    /**
     * 加入布隆过滤器
     * @param key 键值
     * @return
     */
    public void addToBloomFilter(final String key) {
        log.info("加入布隆过滤器\tkey[{}]", key);
        redisBloomFilter.add(key);
    }

    /**
     * 布隆过滤器是否存在该键值
     * @param key 键值
     * @return
     */
    public boolean containsInBloomFilter(final String key) {
        boolean flag = redisBloomFilter.contains(key);
        log.info("key[{}]\t是否存在于布隆过滤器:\t{}", key, flag);
        return flag;
    }

}