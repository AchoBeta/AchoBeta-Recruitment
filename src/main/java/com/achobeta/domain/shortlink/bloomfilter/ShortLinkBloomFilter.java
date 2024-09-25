package com.achobeta.domain.shortlink.bloomfilter;

import com.achobeta.domain.shortlink.model.converter.ShortLinkConverter;
import com.achobeta.redis.bloomfilter.BloomFilterProperties;
import com.achobeta.redis.bloomfilter.RedisBloomFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-24
 * Time: 17:42
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class ShortLinkBloomFilter extends RedisBloomFilter<String> implements InitializingBean {

    private final RedissonClient redissonClient;

    private final ShortLinkBloomFilterProperties shortLinkBloomFilterProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取配置
        BloomFilterProperties properties = ShortLinkConverter.INSTANCE.shortLinkBloomFilterPropertiesToBloomFilterProperties(shortLinkBloomFilterProperties);
        // 初始化布隆过滤器
        initFilter(redissonClient, properties);
    }
}
