package com.achobeta.domain.users.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/1/19
 */
@Data
@Builder
public class LoginVO implements Serializable {
    //token
    String access_token;
    //token有效持续时间
    long expires_in;

}
