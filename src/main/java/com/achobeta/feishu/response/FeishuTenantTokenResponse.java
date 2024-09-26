package com.achobeta.feishu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:34
 */
@Data
public class FeishuTenantTokenResponse extends FeishuResponse {

    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;

    @JsonProperty("expire")
    private Integer expire;

}
