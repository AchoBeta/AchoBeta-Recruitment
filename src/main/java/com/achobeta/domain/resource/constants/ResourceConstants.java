package com.achobeta.domain.resource.constants;

import com.achobeta.domain.resource.enums.ResourceAccessLevel;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 16:36
 */
public interface ResourceConstants {

    ResourceAccessLevel DEFAULT_RESOURCE_ACCESS_LEVEL = ResourceAccessLevel.USER_ACCESS;

    String REDIS_USER_UPLOAD_LIMIT = "userUploadLimit:";

}
