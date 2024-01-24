package com.achobeta.exception;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-01-18
 * Time: 15:44
 */
@Getter
public class GlobalServiceException extends RuntimeException{

    private final GlobalServiceStatusCode statusCode;

    private final String message;

    public GlobalServiceException(String message, GlobalServiceStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public GlobalServiceException(String message) {
        this.message = message;
        this.statusCode = GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL;
    }

    public GlobalServiceException(GlobalServiceStatusCode statusCode) {
        this.message = statusCode.getMessage();
        this.statusCode = statusCode;
    }

    public GlobalServiceException() {
        this.message = GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL.getMessage();
        this.statusCode = GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL;
    }
}
