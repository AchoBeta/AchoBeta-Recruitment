package com.achobeta.domain.resource.access.strategy;

import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:41
 */
@Service
@RequiredArgsConstructor
public class LoginAccessStrategy implements ResourceAccessStrategy {

    private final UserInterceptor userInterceptor;

    @Override
    public boolean isAccessible(DigitalResource resource) {
        try {
            userInterceptor.getUserHelper();
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
