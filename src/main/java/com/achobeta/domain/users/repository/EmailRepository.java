package com.achobeta.domain.users.repository;

import com.achobeta.util.RedisCache;
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

    public void setIdentifyingCode(String redisKey, String code, long timeout) {
        Map<String, Object> data = new HashMap<>();
        data.put(IdentifyingCodeValidator.IDENTIFYING_CODE, code); // 验证码
        data.put(IdentifyingCodeValidator.IDENTIFYING_DEADLINE, System.currentTimeMillis() + timeout); // 键值失效的时间点
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
