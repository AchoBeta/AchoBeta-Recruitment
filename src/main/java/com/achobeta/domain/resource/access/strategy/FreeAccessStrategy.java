package com.achobeta.domain.resource.access.strategy;

import com.achobeta.domain.resource.model.entity.DigitalResource;
import org.springframework.stereotype.Service;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:28
 */
@Service
public class FreeAccessStrategy implements ResourceAccessStrategy {


    @Override
    public boolean isAccessible(DigitalResource resource) {
        return Boolean.TRUE;
    }
}
