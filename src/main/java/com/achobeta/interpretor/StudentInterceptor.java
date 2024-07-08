package com.achobeta.interpretor;

import com.achobeta.common.enums.GlobalServiceStatusCode;
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

import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 10:24
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StudentInterceptor implements HandlerInterceptor {

    private final static Integer USER_TYPE = 1; // 学生

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取登录的信息
        UserHelper currentUser = BaseContext.getCurrentUser();
        if(Objects.isNull(currentUser)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        // 判断是不是学生
        if(!USER_TYPE.equals(currentUser.getRole())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        return true;
    }
}
