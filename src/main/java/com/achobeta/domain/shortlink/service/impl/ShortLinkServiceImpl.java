package com.achobeta.domain.shortlink.service.impl;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.shortlink.bloomfilter.ShortLinkBloomFilter;
import com.achobeta.domain.shortlink.model.converter.ShortLinkConverter;
import com.achobeta.domain.shortlink.model.dao.mapper.ShortLinkMapper;
import com.achobeta.domain.shortlink.model.dao.po.ShortLink;
import com.achobeta.domain.shortlink.model.dto.ShortLinkQueryDTO;
import com.achobeta.domain.shortlink.model.vo.ShortLinkQueryVO;
import com.achobeta.domain.shortlink.service.ShortLinkService;
import com.achobeta.domain.shortlink.util.ShortLinkUtils;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.cache.RedisCache;
import com.achobeta.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.achobeta.domain.shortlink.constants.ShortLinkConstants.*;

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

    private final RedisCache redisCache;

    private final ShortLinkBloomFilter shortLinkBloomFilter;

    @Override
    public Optional<ShortLink> getShortLink(Long id) {
        return this.lambdaQuery()
                .eq(ShortLink::getId, id)
                .oneOpt();
    }

    @Override
    public Optional<ShortLink> getShortLinkByCode(String code) {
        return this.lambdaQuery()
                .eq(ShortLink::getShortCode, code)
                .oneOpt();
    }


    @Override
    public String transShortLinkURL(String baseUrl, String url) {
        //获取短链接code
        String code = url;
        String redisKey = null;
        // 生成唯一的code
        do {
            code = ShortLinkUtils.getShortCodeByURL(code);
            redisKey = REDIS_SHORT_LINK + code;
        } while (shortLinkBloomFilter.contains(redisKey));//误判为存在也无所谓，无非就是再重新生成一个
        // 保存
        ShortLink shortLink = new ShortLink();
        shortLink.setOriginUrl(url);
        shortLink.setShortCode(code);
        log.info("原链接:{} -> redisKey:{}", url, redisKey);
        this.save(shortLink);
        // 缓存到Redis，加入布隆过滤器
        redisCache.setCacheObject(redisKey, url, SHORT_LINK_TIMEOUT, SHORT_LINK_UNIT);
        shortLinkBloomFilter.add(redisKey);
        // 返回完整的短链接
        return HttpRequestUtil.buildUrl(baseUrl,null, code);
    }

    @Override
    public String getOriginUrl(String code) {
        String redisKey = REDIS_SHORT_LINK + code;
        // 更新为已使用
        this.lambdaUpdate()
                .eq(ShortLink::getShortCode, code)
                .set(ShortLink::getIsUsed, Boolean.TRUE)
                .set(ShortLink::getUpdateTime, LocalDateTime.now())
                .update();
        //如果Redis缓存了，就直接返回Redis的值
        return (String) redisCache.getCacheObject(redisKey).orElseGet(() -> {
            //否则查MySQL
            String originUrl = getShortLinkByCode(code)
                    .orElseThrow(() -> new GlobalServiceException("不存在此短链接code：" + code, GlobalServiceStatusCode.PARAM_NOT_VALID))
                    .getOriginUrl();
            // 缓存到Redis里
            redisCache.setCacheObject(redisKey, originUrl, SHORT_LINK_TIMEOUT, SHORT_LINK_UNIT);
            return originUrl;
        });
    }

    @Override
    public ShortLinkQueryVO queryShortLinkList(ShortLinkQueryDTO shortLinkQueryDTO) {
        // 解析分页参数获取 page
        IPage<ShortLink> page = Optional.ofNullable(shortLinkQueryDTO)
                .map(ShortLinkConverter.INSTANCE::shortLinkQueryDTOToBasePageQuery)
                .orElseGet(BasePageQuery::new)
                .toMpPage();
        // 分页
        IPage<ShortLink> resourceIPage = this.page(page);
        // 封装
        BasePageResult<ShortLink> pageResult = BasePageResult.of(resourceIPage);
        // 转化
        return ShortLinkConverter.INSTANCE.basePageResultToShortLinkQueryVO(pageResult);
    }

    @Override
    public void removeShortLink(Long id) {
        getShortLink(id).ifPresent(shortLink -> {
            // 删除数据库对应的行
            this.lambdaUpdate()
                    .eq(ShortLink::getId, id)
                    .remove();
            // 删除缓存（哪怕出现并发问题，影响也不大，也就在缓存期间短链仍有效罢了）
            redisCache.deleteObject(REDIS_SHORT_LINK + shortLink.getShortCode());
        });

    }

}




