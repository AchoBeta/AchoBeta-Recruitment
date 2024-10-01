package com.achobeta.domain.login.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 密码登录才会用到这个 DTO 注册，其余都是直接新建
 *
 * @author BanTanger 半糖
 * @date 2024/1/22 23:06
 */
@Getter
@Setter
@ToString
public class RegisterDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户类型（已废弃，不对外开放，防止恶意注册管理员账号，管理员账号采用人工手动注册）
     */
//    private String userType;

//    @NotBlank(message = "邮箱不能为空")
    private String email;

}
