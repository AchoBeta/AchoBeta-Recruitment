package com.achobeta.domain.resource.access.strategy;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.exception.GlobalServiceException;

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

    default GlobalServiceException failed(DigitalResource resource) {
        return new GlobalServiceException(
                String.format("user %d 尝试访问资源 %s，已阻拦", resource.getUserId(), resource.getCode()),
                GlobalServiceStatusCode.RESOURCE_CANNOT_BE_ACCESSED
        );
    }

}
