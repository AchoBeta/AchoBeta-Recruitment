package com.achobeta.feishu.token;

import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.util.FeishuRequestUtil;
import com.achobeta.util.TimeUtil;
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
public class FeishuTenantAccessToken {

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
