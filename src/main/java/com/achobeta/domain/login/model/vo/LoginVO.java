package com.achobeta.domain.login.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * token
     */
    @JsonProperty("access_token")
    String accessToken;

    /**
     * token 有效持续时间
     */
    @JsonProperty("expires_in")
    long expiresIn;

}
