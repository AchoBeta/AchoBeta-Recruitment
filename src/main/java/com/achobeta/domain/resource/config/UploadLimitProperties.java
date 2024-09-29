package com.achobeta.domain.resource.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 23:26
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "ab.oss.upload-limit")
public class UploadLimitProperties {

    private Integer frequency;

    private Long cycle;

    private TimeUnit unit;

}
