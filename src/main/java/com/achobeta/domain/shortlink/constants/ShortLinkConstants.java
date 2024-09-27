package com.achobeta.domain.shortlink.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-27
 * Time: 17:16
 */
public interface ShortLinkConstants {

    String REDIS_SHORT_LINK = "redisShortLink:";//前缀

    Long SHORT_LINK_TIMEOUT = 7L; // 超时时间 (默认七天)

    TimeUnit SHORT_LINK_UNIT = TimeUnit.DAYS;

}
