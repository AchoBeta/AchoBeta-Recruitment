package com.achobeta.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCache {

    private final RedisTemplate redisTemplate;

    private final RedisBloomFilter redisBloomFilter;

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        log.info("为 Redis 的键值设置超时时间\t[{}]-[{}s]", key, timeout / 1000L);
        return redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        log.info("存入 Redis\t[{}]-[{}]", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timout 超时时间
     */
    public <T> void setCacheObject(final String key, final T value, final long timout) {
        log.info("存入 Redis\t[{}]-[{}]，超时时间:[{}s]", key, value, timout / 1000L);
        redisTemplate.opsForValue().set(key, value, timout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取键值
     * @param key 键
     * @return 键对应的值，并封装成 Optional 对象
     * @param <T>
     */
    public <T> Optional<T> getCacheObject(final String key) {
        T value = (T) redisTemplate.opsForValue().get(key);
        log.info("查询 Redis\t[{}]-[{}]", key, value);
        return Optional.ofNullable(value);
    }
 
    /**
     * 删除单个对象
     * @param key
     */
    public boolean deleteObject(final String key) {
        log.info("删除 Redis 的键值\tkey[{}]", key);
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

    /**
     * 缓存Map
     *
     * @param key
     * @param data
     */
    public <T> void setCacheMap(final String key, final Map<String, T> data) {
        if (Objects.nonNull(data)) {
            log.info("Map 存入 Redis\t[{}]-[{}]", key, data);
            redisTemplate.opsForHash().putAll(key, data);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Optional<Map<String,T>> getCacheMap(final String key) {
        Map<String, T> data = redisTemplate.opsForHash().entries(key);
        data = data.size() == 0 ? null: data;
        log.info("获取 Redis 中的 Map 缓存\t[{}]-[{}]", key, data);
        return Optional.ofNullable(data);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hashKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hashKey, final T value) {
        log.info("存入 Redis 的某个 Map\t[{}.{}]-[{}]", key, hashKey, value);
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hashKey Hash键
     * @return Hash中的对象
     */
    public <T> Optional<T> getCacheMapValue(final String key, final String hashKey) {
        T value = (T) redisTemplate.opsForHash().get(key, hashKey);
        log.info("获取 Redis 中的 Map 的键值\t[{}.{}]-[{}]", key, hashKey, value);
        return Optional.ofNullable(value);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param hashKey
     */
    public void delCacheMapValue(final String key, final String hashKey) {
        log.info("删除 Redis 中的 Map 的键值\tkey[{}.{}]", key, hashKey);
        redisTemplate.opsForHash().delete(key, hashKey);
    }

}