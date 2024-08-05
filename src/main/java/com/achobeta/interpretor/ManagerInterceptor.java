package com.achobeta.interpretor;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.exception.GlobalServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.GeneralSecurityException;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 10:25
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ManagerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取登录的信息
        UserHelper currentUser = BaseContext.getCurrentUser();
        if(Objects.isNull(currentUser)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        // 判断是不是管理员
        if(!UserTypeEnum.ADMIN.getCode().equals(currentUser.getRole())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        return true;
    }
}
