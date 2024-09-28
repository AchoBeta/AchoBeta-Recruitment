package com.achobeta.feishu.config;

import com.lark.oapi.Client;
import com.lark.oapi.service.auth.v3.model.InternalTenantAccessTokenReqBody;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ab.feishu")
public class FeishuAppConfig {

    private OwnerProperties owner;

    private ResourceProperties resource;

    private InternalTenantAccessTokenReqBody credentials;

    @Bean
    public Client feishuClient() {
        // 如果不会用 client，可以直接发请求访问
        return Client.newBuilder(credentials.getAppId(), credentials.getAppSecret()).build();
    }

}
