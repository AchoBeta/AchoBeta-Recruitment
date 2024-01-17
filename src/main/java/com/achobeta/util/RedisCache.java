package com.achobeta.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        // todo: 设置超时时间
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取键值
     * @param key 键
     * @return 键对应的值，并封装成 Optional 对象
     * @param <T>
     */
    public <T> Optional<T> getCacheObject(final String key) {
        T value = (T) redisTemplate.opsForValue().get(key);
        log.info("查询Redis\t[{}]-[{}]", key, value);
        return Optional.ofNullable(value);
    }
 
    /**
     * 删除单个对象
     * @param key
     */
    public boolean deleteObject(final String key) {
        log.info("删除Redis键值\tkey[{}]", key);
        return redisTemplate.delete(key);
    }

    /**
     * 加入布隆过滤器
     * @param bloomFilterName 隆过滤器的名字
     * @param key key 键
     */
    public void addToBloomFilter(final String bloomFilterName, final String key) {
        log.info("加入布隆过滤器[{}]\tkey[{}]", bloomFilterName, key);
        redisBloomFilter.add(bloomFilterName, key);
    }

    /**
     * 布隆过滤器是否存在该键值
     * @param bloomFilterName 布隆过滤器的名字
     * @param key 键
     * @return 键是否存在
     */
    public boolean containsInBloomFilter(final String bloomFilterName, final String key) {
        boolean flag = redisBloomFilter.contains(bloomFilterName, key);
        log.info("key[{}]\t是否存在于布隆过滤器[{}]:\t{}", key, bloomFilterName, flag);
        return flag;
    }

}