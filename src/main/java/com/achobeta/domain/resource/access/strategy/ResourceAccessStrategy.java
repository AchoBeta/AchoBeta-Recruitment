package com.achobeta.domain.resource.access.strategy;

import com.achobeta.domain.resource.model.entity.DigitalResource;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:25
 */
public interface ResourceAccessStrategy {

    String BASE_NAME = "AccessStrategy";

    boolean isAccessible(DigitalResource resource);

}
