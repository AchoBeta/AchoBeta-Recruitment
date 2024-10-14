package com.achobeta.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-13
 * Time: 22:14
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties("ab.mail")
public class EmailSenderConfig {

    private String strategy;

    private List<EmailSenderProperties> senders;

}
