package com.achobeta.domain.users.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:55
 */
@Data
public class UserDTO {

    @NotBlank(message = "昵称不能为空")
    private String nickname;

//    @NotNull(message = "头像不能为空")
    private Long avatar;

}
