package com.achobeta.feishu.constants;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:24
 */
public interface FeishuConstants {

    String AUTHORIZATION_HEADER = "Authorization";

    String AUTHORIZATION_PREFIX = "Bearer ";

    static String getAuthorization(String accessToken) {
        return Optional.ofNullable(accessToken)
                .map(token -> AUTHORIZATION_PREFIX + token)
                .orElse(null);
    }

}
