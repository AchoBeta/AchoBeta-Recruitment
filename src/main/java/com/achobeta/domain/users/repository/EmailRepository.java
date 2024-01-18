package com.achobeta.domain.users.repository;

import com.achobeta.redis.RedisCache;
import com.achobeta.domain.users.util.IdentifyingCodeValidator;
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
     * @param redisKey 用户邮箱在 redis 对应的key
     * @param code 验证码
     * @param timeout 超时时间点
     * @param opportunities 验证机会
     */
    public void setIdentifyingCode(String redisKey, String code, long timeout, int opportunities) {
        Map<String, Object> data = new HashMap<>();
        data.put(IdentifyingCodeValidator.IDENTIFYING_CODE, code); // 验证码
        data.put(IdentifyingCodeValidator.IDENTIFYING_DEADLINE, System.currentTimeMillis() + timeout); // 键值失效的时间点
        data.put(IdentifyingCodeValidator.IDENTIFYING_OPPORTUNITIES, opportunities);
        // todo: 设置超时时间
        redisCache.setCacheObject(redisKey, data);
    }

    public void setIdentifyingCode(String redisKey, Object data) {
        // todo: 设置超时时间
        redisCache.setCacheObject(redisKey, data);
    }

    public Optional getIdentifyingCode(String redisKey) {
        return redisCache.getCacheObject(redisKey);
    }

    public void deleteIdentifyingCodeRecord(String redisKey) {
        redisCache.deleteObject(redisKey);
    }
}
