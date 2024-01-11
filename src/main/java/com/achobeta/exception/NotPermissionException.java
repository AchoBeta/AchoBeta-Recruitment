package com.achobeta.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 19:53
 */
@Getter
@Setter
@AllArgsConstructor
public class NotPermissionException extends RuntimeException {

    private String msg;
    private Integer code;

}
