package com.achobeta.redis;

import com.achobeta.redis.component.RedisBloomFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisCache {

    private final RedisTemplate redisTemplate;

    private final RedisBloomFilter redisBloomFilter;

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        log.info("为 Redis 的键值设置超时时间\t[{}]-[{}s]", key, TimeUnit.MILLISECONDS.toSeconds(timeout));
        return redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获得对象的剩余存活时间
     *
     * @param key 键
     * @return 剩余存活时间
     */
    public long getKeyTTL(final String key) {
        int ttl = Math.toIntExact(redisTemplate.opsForValue().getOperations().getExpire(key));
        String message = null;
        switch (ttl) {
            case -1:
                message = "没有设置过期时间";
                break;
            case -2:
                message = "key不存在";
                break;
            default:
                message = ttl + "s";
                break;
        }
        log.info("查询 Redis key[{}] 剩余存活时间:{}", key, message);
        return TimeUnit.SECONDS.toMillis(ttl); // 统一单位为 ms
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        log.info("存入 Redis\t[{}]-[{}]", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key    缓存的键值
     * @param value  缓存的值
     * @param timeout 超时时间
     */
    public <T> void setCacheObject(final String key, final T value, final long timeout) {
        log.info("存入 Redis\t[{}]-[{}]，超时时间:[{}s]", key, value, TimeUnit.MILLISECONDS.toSeconds(timeout));
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取键值
     *
     * @param key 键
     * @param <T>
     * @return 键对应的值，并封装成 Optional 对象
     */
    public <T> Optional<T> getCacheObject(final String key) {
        T value = (T) redisTemplate.opsForValue().get(key);
        log.info("查询 Redis\t[{}]-[{}]", key, value);
        return Optional.ofNullable(value);
    }

    /**
     * 让指定 Redis 键值进行自减
     *
     * @param key 键
     * @return 自减后的值
     */
    public long decrementCacheNumber(final String key) {
        long number = redisTemplate.opsForValue().decrement(key);
        log.info("Redis key[{}] 自减后：{}", key, number);
        return number;
    }

    /**
     * 让指定 Redis 键值进行自增
     *
     * @param key 键
     * @return 自增后的值
     */
    public long incrementCacheNumber(final String key) {
        long number = redisTemplate.opsForValue().increment(key);
        log.info("Redis key[{}] 自增后：{}", key, number);
        return number;
    }

    /**
     * 加入布隆过滤器
     *
     * @param bloomFilterName 隆过滤器的名字
     * @param key             key 键
     */
    public void addToBloomFilter(final String bloomFilterName, final String key) {
        log.info("加入布隆过滤器[{}]\tkey[{}]", bloomFilterName, key);
        redisBloomFilter.add(bloomFilterName, key);
    }

    /**
     * 布隆过滤器是否存在该键值
     *
     * @param bloomFilterName 布隆过滤器的名字
     * @param key             键
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
    public <T> Optional<Map<String, T>> getCacheMap(final String key) {
        Map<String, T> data = redisTemplate.opsForHash().entries(key);
        data = data.size() == 0 ? null : data;
        log.info("获取 Redis 中的 Map 缓存\t[{}]-[{}]", key, data);
        return Optional.ofNullable(data);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key     Redis键
     * @param hashKey Hash键
     * @param value   值
     */
    public <T> void setCacheMapValue(final String key, final String hashKey, final T value) {
        log.info("存入 Redis 的某个 Map\t[{}.{}]-[{}]", key, hashKey, value);
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key     Redis键
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

    /**
     * 让指定 HashMap 的键值进行自减
     *
     * @param key     HashMap的名字
     * @param hashKey HashMap的一个键
     * @return 自减后的值
     */
    public long decrementCacheMapNumber(final String key, final String hashKey) {
        long number = redisTemplate.opsForHash().increment(key, hashKey, -1);
        log.info("Redis key[{}.{}] 自减后：{}", key, hashKey, number);
        return number;
    }

    /**
     * 让指定 HashMap 的键值进行自增
     *
     * @param key     HashMap的名字
     * @param hashKey HashMap的一个键
     * @return 自增后的值
     */
    public long incrementCacheMapNumber(final String key, final String hashKey) {
        long number = redisTemplate.opsForHash().increment(key, hashKey, +1);
        log.info("Redis key[{}.{}] 自增后：{}", key, hashKey, number);
        return number;
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        log.info("删除 Redis 的键值\tkey[{}]", key);
        return redisTemplate.delete(key);
    }

    /**
     * 原子设置过期时间
     * @param key
     * @param value
     * @param timeout
     */
    public <T> void execute(final String key, final T value, final long timeout) {
        log.info("尝试存入 Redis\t[{}]-[{}]，超时时间:[{}s]", key, value, TimeUnit.MILLISECONDS.toSeconds(timeout));
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().set((K) key, (V) value);
                operations.expire((K) key, timeout, TimeUnit.MILLISECONDS);
                return operations.exec();
            }
        });
    }

}