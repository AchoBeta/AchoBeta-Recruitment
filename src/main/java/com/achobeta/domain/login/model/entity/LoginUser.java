package com.achobeta.domain.login.model.entity;

import com.achobeta.common.enums.UserTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 14:14
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginUser implements Serializable {

    /**
     * 用户 id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户类型
     */
    private int userType;

    public boolean isAdmin() {
        return UserTypeEnum.ADMIN.getCode().equals(this.userType);
    }

}
