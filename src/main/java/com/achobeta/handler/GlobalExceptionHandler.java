package com.achobeta.handler;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.achobeta.common.enums.GlobalServiceStatusCode.PARAM_FAILED_VALIDATE;

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

    @ExceptionHandler(GlobalServiceException.class)
    public SystemJsonResponse handleGlobalServiceException(GlobalServiceException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String message = e.getMessage();
        GlobalServiceStatusCode statusCode = e.getStatusCode();
        log.error("请求地址'{}', {}: '{}'", requestURI, statusCode, message);
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(statusCode, message);
    }

    @ExceptionHandler({FileUploadException.class})
    public SystemJsonResponse handleFileUploadException(FileUploadException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String message = e.getMessage();
        GlobalServiceStatusCode statusCode = GlobalServiceStatusCode.RESOURCE_OUT_SIZE;
        log.error("请求地址'{}', {}: '{}'", requestURI, statusCode, message);
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(statusCode, message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public SystemJsonResponse constraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        log.error("请求地址'{}', 自定义验证异常'{}'", requestURI, e.getMessage());
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SystemJsonResponse ValidationHandler(MethodArgumentNotValidException e, HttpServletRequest request){
        log.error("数据校验出现问题，异常类型:{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item)->{
            String message = item.getDefaultMessage();
            // 获取错误的属性字段名
            String field = item.getField();
            map.put(field,message);
        });

        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE,map.toString());
    }
}
