package com.achobeta.domain.shortlink.service.impl;

import com.achobeta.util.RedisCache;
import com.achobeta.domain.shortlink.mapper.ShortLinkMapper;
import com.achobeta.domain.shortlink.po.ShortLink;
import com.achobeta.domain.shortlink.service.ShortLinkService;
import com.achobeta.domain.shortlink.util.ShortLinkUtils;
import com.achobeta.exception.ShortLinkGenerateException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【short_link】的数据库操作Service实现
* @createDate 2024-01-12 19:48:07
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink>
    implements ShortLinkService {

    private static final String BLOOM_FILTER_NAME = "LINK-CODE-LIST";

    private static final long SHORT_LINK_TIMEOUT = 1 * 365 * 24 * 3600 * 1000L; // 超时时间

    private final RedisCache redisCache;

    @Override
    public String transShortLinkURL(String baseUrl, String url) {
        //获取短链接code
        String code = url;
        String redisKey = null;
        // 生成唯一的code
        do {
            code = ShortLinkUtils.getShortCodeByURL(code);
            redisKey = ShortLinkUtils.REDIS_SHORT_LINK + code;
        } while (redisCache.containsInBloomFilter(BLOOM_FILTER_NAME, redisKey));//误判为存在也无所谓，无非就是再重新生成一个
        // 保存
        ShortLink shortLink = new ShortLink();
        shortLink.setOriginUrl(url);
        shortLink.setShortCode(code);
        shortLink.setDeleted(0);
        shortLink.setVersion(0);
        shortLink.setIsUsed(false);
        shortLink.setCreateTime(LocalDateTime.now());
        log.info("原链接:{} -> redisKey:{}", url, redisKey);
        this.save(shortLink);
        // 缓存到Redis，加入布隆过滤器
        redisCache.setCacheObject(redisKey, url, SHORT_LINK_TIMEOUT);
        redisCache.addToBloomFilter(BLOOM_FILTER_NAME, redisKey);
        // 返回完整的短链接
        return baseUrl + code;
    }

    @Override
    public String getOriginUrl(String code) {
        String redisKey = ShortLinkUtils.REDIS_SHORT_LINK + code;
        //如果Redis缓存了，就直接返回Redis的值
        Optional<String> originUrlCache = redisCache.getCacheObject(redisKey);
        return originUrlCache.orElseGet(() -> {
            //否则查MySQL
            ShortLink shortLink = this.lambdaQuery().eq(ShortLink::getShortCode, code).one();
            if(Objects.isNull(shortLink)) {
                throw new ShortLinkGenerateException("不存在此短链接code：" + code);
            }
            String originUrl = shortLink.getOriginUrl();
            // 缓存到Redis里
            redisCache.setCacheObject(redisKey, originUrl, SHORT_LINK_TIMEOUT);
            redisCache.addToBloomFilter(BLOOM_FILTER_NAME, redisKey);
            return originUrl;
        });
    }


}




