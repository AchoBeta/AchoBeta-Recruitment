package com.achobeta.feishu.token;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.util.FeishuRequestUtil;
import com.achobeta.util.GsonUtil;
import com.achobeta.util.TimeUtil;
import com.lark.oapi.Client;
import com.lark.oapi.service.auth.v3.model.InternalTenantAccessTokenReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
public class FeishuTenantAccessToken {

    private final Client feishuClient;

    private final FeishuAppConfig feishuAppConfig;

    private String tenantAccessToken;

    private Integer expire;


    private long compareToNow(int tokenExpire) {
        return TimeUtil.millisToSecond(tokenExpire) - System.currentTimeMillis();
    }

    private boolean shouldRefresh() {
        return !StringUtils.hasText(tenantAccessToken) || Optional.ofNullable(expire)
                // 这里判断的一般是准确的，如果非本类请求到的 token，可能会出现到期前三十分钟获取另一个 token，那也不影响这个 token 的有效性
                .filter(tokenExpire -> compareToNow(tokenExpire) > 0)
                .isPresent();
    }

    private void refreshToken() {
//        try {
//            InternalTenantAccessTokenReq tenantAccessTokenReq = InternalTenantAccessTokenReq.newBuilder()
//                    .internalTenantAccessTokenReqBody(feishuAppConfig.getToken())
//                    .build();
//            byte[] bytes = feishuClient.auth()
//                    .tenantAccessToken()
//                    .internal(tenantAccessTokenReq)
//                    .getRawResponse()
//                    .getBody();
//            FeishuTenantTokenResponse responseBody = GsonUtil.fromBytes(bytes, FeishuTenantTokenResponse.class);
//            FeishuRequestUtil.checkResponse(responseBody);
//            this.tenantAccessToken = responseBody.getTenantAccessToken();
//            this.expire = responseBody.getExpire();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        FeishuTenantTokenResponse responseBody = FeishuRequestUtil.request(
                HttpRequestEnum.TENANT_ACCESS_TOKEN,
                feishuAppConfig.getToken(),
                FeishuTenantTokenResponse.class,
                null
        );
        this.tenantAccessToken = responseBody.getTenantAccessToken();
        this.expire = responseBody.getExpire();
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
