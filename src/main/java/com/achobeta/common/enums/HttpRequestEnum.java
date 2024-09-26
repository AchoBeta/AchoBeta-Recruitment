package com.achobeta.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:29
 */
@Getter
@AllArgsConstructor
public enum HttpRequestEnum {

    TENANT_ACCESS_TOKEN("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal", "POST"),

    GET_USER_ID("https://open.feishu.cn/open-apis/contact/v3/users/batch_get_id?user_id_type=user_id", "POST"),

    RESERVE_APPLY("https://open.feishu.cn/open-apis/vc/v1/reserves/apply?user_id_type=user_id", "POST"),


    ;

    private final String url;

    private final String method;

}
