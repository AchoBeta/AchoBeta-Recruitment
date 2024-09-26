package com.achobeta.feishu.token;

import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.feishu.config.FeishuTokenProperties;
import com.achobeta.feishu.response.FeishuTenantTokenResponse;
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

    private final static Integer SUCCESS_CODE = 0;

    private final FeishuTokenProperties feishuTokenProperties;

    private String tenantAccessToken;

    private Integer expire;


    private long compareToNow(int tokenExpire) {
        return TimeUtil.millisToSecond(tokenExpire) - System.currentTimeMillis();
    }

    private boolean shouldRefresh() {
        return !StringUtils.hasText(tenantAccessToken) || Optional.ofNullable(expire)
                .filter(tokenExpire -> compareToNow(tokenExpire) > 0)
                .isPresent();
    }

    private void refreshToken() {
        FeishuTenantTokenResponse feishuTenantTokenVO = FeishuRequestUtil.request(
                HttpRequestEnum.TENANT_ACCESS_TOKEN,
                feishuTokenProperties,
                FeishuTenantTokenResponse.class,
                null
        );
        this.tenantAccessToken = feishuTenantTokenVO.getTenantAccessToken();
        this.expire = feishuTenantTokenVO.getExpire();
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
