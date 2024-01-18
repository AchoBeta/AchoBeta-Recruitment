package com.achobeta.domain.users.repository;

import com.achobeta.domain.users.util.IdentifyingCodeValidator;
import com.achobeta.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class EmailRepository {

    private final RedisCache redisCache;

    /**
     * 设置验证码到 redis 里（携带超时时间点，与验证机会数）
     *
     * @param redisKey      用户邮箱在 redis 对应的key
     * @param code          验证码
     * @param timeout       超时时间点
     * @param opportunities 验证机会
     */
    public void setIdentifyingCode(String redisKey, String code, long timeout, int opportunities) {
        Map<String, Object> data = new HashMap<>();
        data.put(IdentifyingCodeValidator.IDENTIFYING_CODE, code); // 验证码
        data.put(IdentifyingCodeValidator.IDENTIFYING_DEADLINE, System.currentTimeMillis() + timeout); // 键值失效的时间点
        data.put(IdentifyingCodeValidator.IDENTIFYING_OPPORTUNITIES, opportunities); // 有效次数
        redisCache.setCacheMap(redisKey, data);
        redisCache.expire(redisKey, timeout); // 设置超时时间
    }

    public void setIdentifyingCodeOpportunities(String redisKey, int opportunities, long timeout) {
        redisCache.setCacheMapValue(redisKey, IdentifyingCodeValidator.IDENTIFYING_OPPORTUNITIES, opportunities);
        redisCache.expire(redisKey, timeout); // 更新超时时间
    }

    public <T> Optional<Map<String,T>> getIdentifyingCode(String redisKey) {
        return redisCache.getCacheMap(redisKey);
    }

    public void deleteIdentifyingCodeRecord(String redisKey) {
        redisCache.deleteObject(redisKey);
    }
}
