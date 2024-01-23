package com.achobeta.domain.login.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:33
 */
@Getter
@Setter
@ToString
public class PasswordLoginDTO extends LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
