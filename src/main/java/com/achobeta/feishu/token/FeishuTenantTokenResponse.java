package com.achobeta.feishu.token;

import com.google.gson.annotations.SerializedName;
import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.core.response.EmptyData;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:34
 */
@Data
public class FeishuTenantTokenResponse extends BaseResponse<EmptyData> {

    @SerializedName("tenant_access_token")
    private String tenantAccessToken;

    @SerializedName("expire")
    private Integer expire;

}
