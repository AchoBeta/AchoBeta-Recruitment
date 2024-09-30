package com.achobeta.domain.shortlink.service;


import com.achobeta.domain.shortlink.model.dao.po.ShortLink;
import com.achobeta.domain.shortlink.model.dto.ShortLinkQueryDTO;
import com.achobeta.domain.shortlink.model.vo.ShortLinkQueryVO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * @author 马拉圈
 * @description 针对表【short_link】的数据库操作Service
 * @createDate 2024-01-12 19:48:07
 */
public interface ShortLinkService extends IService<ShortLink> {

    Optional<ShortLink> getShortLink(Long id);

    Optional<ShortLink> getShortLinkByCode(String code);

    String getSystemUrl(HttpServletRequest request, String code);

    /**
     * url生成唯一的code
     * 1. 将code和源链接的关系保存到数据库里
     * 2. 将code对应的key和源链接作为键值对保存到redis里
     * 3. 将code对应的key加入到redis布隆过滤器中
     *
     * @param url     原链接
     * @return 短链接
     */
    String transShortLinkURL(HttpServletRequest request, String url);

    /**
     * 通过code获取原链接
     * 1. redis中存在短链接code对应的键值对，直接返回重定向原链接
     * 2. 否则，查数据库再重定向，如果查不到就抛异常
     *
     * @param code 短链code
     * @return 原链接
     */
    String getOriginUrl(String code);

    ShortLinkQueryVO queryShortLinkList(ShortLinkQueryDTO shortLinkQueryDTO);

    void removeShortLink(Long id);
}
