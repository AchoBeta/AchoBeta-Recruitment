package com.achobeta.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-17
 * Time: 0:42
 */
public class IllegalUrlException extends IllegalArgumentException {
    public IllegalUrlException(String s) {
        super(s);
    }
}
