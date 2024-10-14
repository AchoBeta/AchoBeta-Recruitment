package com.achobeta.jwt.propertities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author cattleYuan
 * @date 2024/1/18
 */
@Component
@ConfigurationProperties(prefix = "ab.jwt")
@Data
public class JwtProperties {
    /**
     * 用户端用户生成jwt令牌相关配置
     */
    private String secretKey;
    private Long ttl;
    private Long refreshTime;
    private String tokenName;

}