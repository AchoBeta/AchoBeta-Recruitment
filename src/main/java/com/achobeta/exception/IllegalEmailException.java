package com.achobeta.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-17
 * Time: 20:55
 */
public class IllegalEmailException extends RuntimeException {

    public IllegalEmailException(String message) {
        super(message);
    }
}
