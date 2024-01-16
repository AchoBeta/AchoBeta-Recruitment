package com.achobeta.domain.shortlink.component;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Preconditions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisBloomFilter {

    private static final String BLOOM_FILTER_NAME = "LINK-CODE-LIST";


    private final RedisTemplate redisTemplate;

    private BloomFilterHelper bloomFilterHelper = new BloomFilterHelper();

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void add(T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            // todo: 设置超时时间
            redisTemplate.opsForValue().setBit(BLOOM_FILTER_NAME, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean contains(T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(BLOOM_FILTER_NAME, i)) {
                return false;
            }
        }
        return true;
    }

}
