package com.achobeta.handler;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.exception.NotPermissionException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.achobeta.common.constants.GlobalServiceStatusCode.USER_NO_PERMISSION;

/**
 * 全局异常处理器，减少 try-catch 语句
 * <p>
 *     定义方式：将需要抛出的异常(第三方异常、自定义异常、系统异常)
 * 传入注解 {@link ExceptionHandler}，再定义方法制作异常的处理方式
 * </p>
 * @author BanTanger 半糖
 * @date 2024/1/11 19:45
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotPermissionException.class)
    public SystemJsonResponse handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 权限码校验失败'{}'", requestURI, e.getMessage());
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(USER_NO_PERMISSION, "没有访问权限, 请联系管理员授权");
    }

}
