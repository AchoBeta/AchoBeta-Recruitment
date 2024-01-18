package com.achobeta.exception;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */
public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }

}
