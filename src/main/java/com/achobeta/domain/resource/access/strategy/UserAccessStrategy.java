package com.achobeta.domain.resource.access.strategy;

import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.interpretor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 0:03
 */
@Service
@RequiredArgsConstructor
public class UserAccessStrategy implements ResourceAccessStrategy {

    private final UserInterceptor userInterceptor;

    @Override
    public boolean isAccessible(DigitalResource resource) {
        try {
            UserHelper userHelper = userInterceptor.getUserHelper();
            if (UserTypeEnum.ADMIN.getCode().equals(userHelper.getRole())) {
                return Boolean.TRUE;
            } else {
                // 上传者是否为当前用户
                return resource.getUserId().equals(userHelper.getUserId());
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}