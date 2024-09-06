package com.achobeta.domain.users.model.po;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author cattleYuan
 * @date 2024/1/20
 */
@Getter
@ToString
@Builder
public class UserHelper {

    String token;
    long userId;
    //登录角色类型
    Integer role;
}
