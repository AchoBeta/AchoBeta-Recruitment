package com.achobeta.feishu.token;

import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.request.FeishuRequestEngine;
import com.achobeta.util.TimeUtil;
import com.lark.oapi.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 8:06
 */
@Component
@RequiredArgsConstructor
public class FeishuTenantSession {

    private final Client feishuClient;

    private final FeishuAppConfig feishuAppConfig;

    private final FeishuRequestEngine feishuRequestEngine;

    private volatile String tenantAccessToken;

    private volatile Long expire;

    private boolean shouldRefresh() {
        return !StringUtils.hasText(tenantAccessToken) || Optional.ofNullable(expire)
                // 这里判断的一般是准确的，如果非本类请求到的 token，可能会出现到期前三十分钟获取另一个 token，那也不影响这个 token 的有效性
                .filter(tokenExpire -> tokenExpire - System.currentTimeMillis() > 0)
                .isEmpty();
    }

    public void refreshToken() {
//        try {
//            InternalTenantAccessTokenReq tenantAccessTokenReq = InternalTenantAccessTokenReq.newBuilder()
//                    .internalTenantAccessTokenReqBody(feishuAppConfig.getCredentials())
//                    .build();
//            byte[] bytes = feishuClient.auth()
//                    .tenantAccessToken()
//                    .internal(tenantAccessTokenReq)
//                    .getRawResponse()
//                    .getBody();
//            FeishuTenantTokenResponse responseBody = GsonUtil.fromBytes(bytes, FeishuTenantTokenResponse.class);
//            feishuRequestEngine.checkResponse(responseBody);
//            this.tenantAccessToken = responseBody.getTenantAccessToken();
//            this.expire = TimeUtil.secondToMillis(responseBody.getExpire()) + System.currentTimeMillis();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        FeishuTenantTokenResponse responseBody = feishuRequestEngine.jsonRequest(
                HttpRequestEnum.TENANT_ACCESS_TOKEN,
                feishuAppConfig.getCredentials(),
                FeishuTenantTokenResponse.class,
                null
        );
        this.tenantAccessToken = responseBody.getTenantAccessToken();
        this.expire = TimeUtil.secondToMillis(responseBody.getExpire()) + System.currentTimeMillis();
    }

    public String getToken() {
        if(shouldRefresh()) {
            synchronized (this) {
                if(shouldRefresh()) {
                    refreshToken();
                }
            }
        }
        return this.tenantAccessToken;
    }
}
