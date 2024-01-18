package com.achobeta.exception;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 19:53
 */
public class NotPermissionException extends RuntimeException {
    public NotPermissionException(String message) {
        super(message);
    }
}
