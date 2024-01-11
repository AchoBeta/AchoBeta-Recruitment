package com.achobeta.domain.users.service.impl;

import com.achobeta.domain.users.service.UserService;
import com.achobeta.exception.NotPermissionException;
import org.springframework.stereotype.Service;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 20:03
 */
@Service
public class UserServiceImpl implements UserService {

    public void test_NotPermissionExceptionHandler() {
        boolean loginStatus = false;
        if (!loginStatus) {
            // 减少 try-catch 语句, 直接抛出异常将会被全局异常处理器 GlobalExceptionHandler 接收处理
            throw new NotPermissionException();
        }
    }

}
