package com.achobeta.domain.login.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 密码登录才会用到这个 DTO 注册，其余都是直接新建
 * @author BanTanger 半糖
 * @date 2024/1/22 23:06
 */
@Getter
@Setter
public class RegisterDTO extends LoginDTO{

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;

    /**
     * 用户类型（已废弃，不对外开放，防止恶意注册管理员账号，管理员账号采用人工手动注册）
     */
    @Deprecated
    private String userType;

}
