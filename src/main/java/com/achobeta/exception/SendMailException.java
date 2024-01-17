package com.achobeta.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-16
 * Time: 18:26
 */
public class SendMailException extends RuntimeException {
    public SendMailException(String message) {
        super(message);
    }
}
