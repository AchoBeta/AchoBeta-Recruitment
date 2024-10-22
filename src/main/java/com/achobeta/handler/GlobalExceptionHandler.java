package com.achobeta.handler;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.config.RequestIdConfig;
import com.achobeta.exception.GlobalServiceException;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.achobeta.common.enums.GlobalServiceStatusCode.*;

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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final RequestIdConfig requestIdConfig;

    private void logError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("请求 {} 访问接口 {}，错误信息 {}",
                response.getHeader(requestIdConfig.getHeader()),
                request.getRequestURI(),
                e.getMessage()
        );
    }

    @ExceptionHandler(GlobalServiceException.class)
    public SystemJsonResponse handleGlobalServiceException(GlobalServiceException e, HttpServletRequest request, HttpServletResponse response) {
        logError(request, response, e);
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(e.getStatusCode(), e.getMessage());
    }

    @ExceptionHandler({FileUploadException.class})
    public SystemJsonResponse handleFileUploadException(FileUploadException e, HttpServletRequest request, HttpServletResponse response) {
        logError(request, response, e);
        String message = "文件上传异常";
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(RESOURCE_NOT_VALID, message);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public SystemJsonResponse handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request, HttpServletResponse response) {
        logError(request, response, e);
        String message = e.getCause() instanceof MysqlDataTruncation ? "数据截断，请检查长度、范围和类型" : "数据非法";
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(SYSTEM_SERVICE_ERROR, message);
    }

    @ExceptionHandler({SQLException.class})
    public SystemJsonResponse handleSQLException(SQLException e, HttpServletRequest request, HttpServletResponse response) {
        logError(request, response, e);
        String message = "数据访问与交互异常";
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(SYSTEM_SERVICE_ERROR, message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public SystemJsonResponse constraintViolationException(ConstraintViolationException e, HttpServletRequest request, HttpServletResponse response) {
        logError(request, response, e);
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SystemJsonResponse ValidationHandler(MethodArgumentNotValidException e, HttpServletRequest request, HttpServletResponse response) {
        logError(request, response, e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
        return SystemJsonResponse.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE, message);
    }
}
