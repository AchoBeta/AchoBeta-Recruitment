package com.achobeta.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-17
 * Time: 12:14
 */
public class EmailIdentifyingException extends RuntimeException {
    public EmailIdentifyingException(String message) {
        super(message);
    }
}
