package com.achobeta.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-16
 * Time: 18:51
 */
public class ParameterValidateException extends RuntimeException {

    public ParameterValidateException(String message) {
        super(message);
    }
}