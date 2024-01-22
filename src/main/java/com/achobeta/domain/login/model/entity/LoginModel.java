package com.achobeta.domain.login.model.entity;

import lombok.*;

import java.util.Map;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 18:37
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {

    /**
     * 是否为持久 cookie (临时 cookie 在浏览器关闭会自动删除，持久 cookie 则不会)
     */
    private boolean isLastingCookie = true;

    /**
     * token 有效期，单位：秒
     */
    private long timeout;

    /**
     * token 最低活跃频率，单位：秒
     */
    private long activeTimeout;

    /**
     * jwt 扩展信息
     */
    private Map<String, Object> extraData;

    /**
     * 是否将 token 写入响应头
     */
    private boolean isWriteHeader = true;

}
