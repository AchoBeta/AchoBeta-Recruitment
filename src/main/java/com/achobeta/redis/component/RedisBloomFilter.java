
package com.achobeta.redis.component;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisBloomFilter {

    private final RedisTemplate redisTemplate;

    private final BloomFilterHelper bloomFilterHelper;

    public void init(String bloomFilterName) {
        int[] offset = bloomFilterHelper.getHashOffset();
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(bloomFilterName, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void add(String bloomFilterName, T value) {
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(bloomFilterName, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean contains(String bloomFilterName, T value) {
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(bloomFilterName, i)) {
                return false;
            }
        }
        return true;
    }

}
