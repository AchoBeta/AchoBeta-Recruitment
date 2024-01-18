package com.achobeta.redis;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Preconditions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisBloomFilter {

    private final RedisTemplate redisTemplate;

    private BloomFilterHelper bloomFilterHelper = new BloomFilterHelper();

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void add(String bloomFilterName, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            // todo: 设置超时时间
            redisTemplate.opsForValue().setBit(bloomFilterName, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean contains(String bloomFilterName, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(bloomFilterName, i)) {
                return false;
            }
        }
        return true;
    }

}
