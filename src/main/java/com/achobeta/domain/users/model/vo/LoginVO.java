package com.achobeta.domain.users.model.vo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/1/19
 */
@Getter
@Builder
public class LoginVO implements Serializable {
    //token
    String accessToken;
    //token有效持续时间
    long expiresIn;

}
