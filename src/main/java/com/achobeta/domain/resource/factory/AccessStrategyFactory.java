package com.achobeta.domain.resource.factory;

import com.achobeta.domain.resource.access.strategy.ResourceAccessStrategy;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:33
 */
public interface AccessStrategyFactory {

    ResourceAccessStrategy getStrategy(ResourceAccessLevel level);

}
