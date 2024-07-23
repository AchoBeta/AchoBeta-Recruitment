package com.achobeta.redis.strategy;

import org.redisson.api.RLock;

import java.util.function.Function;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-15
 * Time: 14:03
 */
public interface LockStrategy extends Function<String, RLock> {
}
